package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.common.type.AuthType;
import hyundaiautoever.library.model.dto.RentDto;
import hyundaiautoever.library.model.dto.UserDto;
import hyundaiautoever.library.model.dto.UserDto.UserAuthPage;
import hyundaiautoever.library.model.dto.UserDto.UserProfile;
import hyundaiautoever.library.model.dto.request.UserRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.entity.Love;
import hyundaiautoever.library.model.entity.Rent;
import hyundaiautoever.library.model.entity.Reserve;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // 조회 성능 최적화
public class UserService {

    private final UserRepository userRepository;
    private final RentRepository rentRepository;
    private final ReviewRepository reviewRepository;
    private final ApplyRepository applyRepository;
    private final LoveRepository loveRepository;
    private final ReserveRepository reserveRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;


    /**
     * 로그인 login
     */
    @Transactional
    public Response loginUser(UserRequest.LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByPersonalId(request.getPersonalId());
        if (optionalUser.isEmpty()) {
            return Response.personalIdError(ExceptionCode.PERSONALID_ERROR);
        }
        User user = optionalUser.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return Response.passwordError(ExceptionCode.PASSWORD_ERROR);
        }
        user.updateLastLoginDate(LocalDateTime.now());
        return Response.ok().setData(UserDto.buildLoginDto(user));
    }


    /**
     * 회원 가입 JOIN
     */
    @Transactional
    public void createUser(UserRequest.CreateUserRequest request) {
        User user = User.builder()
                .name(request.getName())
                .personalId(request.getPersonalId())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .partType(request.getPartType())
                .nickname(request.getNickname())
                .build();

        if (checkEmail(request.getEmail()) || checkNickname(request.getNickname()) || checkPersonalId(request.getPersonalId())) {
            throw new LibraryException.DataDuplicateException(ExceptionCode.DATA_DUPLICATE_EXCEPTION);
        }

        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("createUser Exception : {}", e.getMessage());
            throw new LibraryException.DataSaveException(ExceptionCode.DATA_SAVE_EXCEPTION);
        }
        user.updateLastLoginDate(LocalDateTime.now());
    }

    /**
     * 회원 전체 조회
     */
    public List<User> findUsers() {
        return userRepository.findAll();
    }


    /**
     * 사용자 프로필 조회
     */
    public UserProfile searchUserProfile(String personalId) {
        return UserDto.buildUserProfile(userRepository.findByPersonalId(personalId).orElseThrow(() ->
                new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION)));
    }

    // 중복 데이터 검증 -> 아이디 중복, 이메일, 닉네임 중복 확인

    /**
     * 아이디 체크
     */
    public boolean checkPersonalId(String personalId) {
        log.info("UserService :: Call checkPersonalId Method!");
        return userRepository.existsByPersonalId(personalId);
    }

    /**
     * 이메일 체크
     */
    public boolean checkEmail(String email) {
        log.info("UserService :: Call checkEmail Method!");
        return userRepository.existsByEmail(email);
    }
    /**
     * 닉네임 체크
     */
    public boolean checkNickname(String nickname) {
        log.info("UserService :: Call checkNickname Method!");
        return userRepository.existsByNickname(nickname);
    }


    /**
     * 유저 프로필 변경
     * @param request
     * @return ok
     */
    @Transactional
    public Response updateProfile(UserRequest.UpdateProfileRequest request) {
        User user = userRepository.findByPersonalId(request.getPersonalId()).orElseThrow(() -> {
            log.error("updateUserPassword Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            log.error("updateUserPassword Exception : [현재 비밀번호 오류]", ExceptionCode.PASSWORD_ERROR);
            return Response.passwordError(ExceptionCode.PASSWORD_ERROR);
        }
        // 비밀번호 변경
        user.updateUserPassword(passwordEncoder.encode(request.getNewPassword()));

        // 이메일 변경
        user.updateUserEmail(request.getEmail() != null ? request.getEmail() : user.getEmail());
        // 닉네임 변경
        user.updateUserNickname(request.getNickname() != null ? request.getNickname() : user.getNickname());

        return Response.ok();
    }


    /**
     * ADMIN 권한 관리
     * @param personalId
     * @param auth
     * @return LoginDto
     */
    @Transactional
    public UserDto.LoginDto updateAuth(String personalId, String auth) {
        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("updateAuth Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        try {
            user.updateUserAuthType(auth.equals("ADMIN") ? AuthType.ADMIN : AuthType.USER);
        } catch (Exception e) {
            log.error("updateAuth Exception : {}", e.getMessage());
            throw new LibraryException.DataUpdateException(ExceptionCode.DATA_UPDATE_EXCEPTION);
        }
        return UserDto.buildLoginDto(user);
    }

    /**
     * 회원 탈퇴
     * @param personalId
     */
    @Transactional
    public Response deleteUser(String personalId) {
        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("deleteUser Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        if (user.getAuthType().equals(AuthType.USER)) {
            List<Rent> rentList = rentRepository.findByUser(user);
            if (!rentList.isEmpty()) { // 대여 중인 도서 존재
                return Response.withdrawException(ExceptionCode.DELETE_RENT_ERROR).setData(rentList.stream().map(RentDto.GetRentDto::new).collect(Collectors.toList())); //304
            }
        }

        // 도서 신청
        applyRepository.deleteAllByUser(user);

        // 좋아요
        List<Love> loveList = loveRepository.findByUser(user);
        if (!loveList.isEmpty()) {
            // 좋아요 -1
            loveList.forEach(love -> love.getBook().updateLoveCount(love.getBook().getLoveCount() - 1));
        }
        loveRepository.deleteAllByUser(user);

        // 대여
        rentRepository.deleteAllByUser(user);

        // 예약
        List<Reserve> reserveList = reserveRepository.findByUser(user); // 유저가 예약한 도서 목록
        if (!reserveList.isEmpty()) { // 예약 목록 존재
            for (Reserve reserve : reserveList) {
                // 다음 예약자가 있는 경우
                Reserve firstReserve = reserveRepository.findByWaitNumberAndBook(reserve.getWaitNumber() + 1, reserve.getBook());
                if (firstReserve != null) {

                    // 도서를 예약한 사용자 리스트
                    List<Reserve> reserveUserList = reserveRepository.findAllByBookAndWaitNumberGreaterThan(reserve.getBook(), reserve.getWaitNumber());

                    // 예약 대기 순번 -1
                    reserveUserList.forEach(reserveUser -> reserveUser.updateWaitNumber(reserveUser.getWaitNumber() - 1));
                }
            }
        }
        reserveRepository.deleteAllByUser(user);

        // 리뷰
        reviewRepository.deleteAllByUser(user);

        // 유저 삭제
        try {
            userRepository.deleteById(user.getId());
        } catch (Exception e) {
            log.error("deleteUser Exception : {}", e.getMessage());
            throw new LibraryException.DataDeleteException(ExceptionCode.DATA_DELETE_EXCEPTION);
        }

        return Response.ok();
    }

    /**
     * 권한 관리 유저 페이지 조회
     * @param pageable
     * @param personalId
     * @param name
     * @return UserAuthPage
     */
    public UserAuthPage searchUserAuthPage(Pageable pageable, String personalId, String name) {
        log.info("UserService : [searchUserAuthPage]");
        return UserDto.buildUserAuthPage(userRepository.searchUserAuthPage(pageable, personalId, name));
    }


    /**
     * 아이디 찾기
     * @param email
     * @return personalId
     */
    public Response getFindId(String email) {
        log.info("UserService : [searchUserAuthPage]");
        User user = userRepository.findByEmail(email);

        // 이메일에 해당하는 User 없음
        if (user == null) {
            return Response.dataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        }
        return Response.ok().setData(user.getPersonalId());
    }

    /**
     * 비밀번호 재설정
     * @param request
     */
    @Transactional
    public void resetPassword(UserRequest.ResetPassword request) {
        User user = userRepository.findByPersonalId(request.getPersonalId()).orElseThrow(() -> {
            log.error("updateUserPassword Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        // 비밀번호 변경
        try {
            user.updateUserPassword(passwordEncoder.encode(request.getNewPassword()));
        } catch (Exception e) {

            log.error("resetPassword Exception : {}", e.getMessage());
            throw new LibraryException.DataUpdateException(ExceptionCode.DATA_UPDATE_EXCEPTION);
        }
    }
}


