package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.common.type.AuthType;
import hyundaiautoever.library.model.dto.UserDto;
import hyundaiautoever.library.model.dto.UserDto.UserAuthPage;
import hyundaiautoever.library.model.dto.request.UserRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final PasswordEncoder passwordEncoder;


    /**
     * 로그인 login
     */
    @Transactional
    public Response loginUser(String personalId, String password) {
        Optional<User> optionalUser = userRepository.findByPersonalId(personalId);
        if (optionalUser.isEmpty()) {
            return Response.personalIdError(ExceptionCode.PERSONALID_ERROR);
        }
        User user = optionalUser.get();
        if (!user.getPassword().equals(password)) {
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
    public UserDto.UserSimpleDto searchUserProfile(String personalId) {
        return UserDto.buildUserSimpleDto(userRepository.findByPersonalId(personalId).orElseThrow(() ->
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
     * 유저 이메일 변경
     * @param personalId
     * @param email
     */
    @Transactional
    public void updateUserEmail(String personalId, String email) {
        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("updateUserEmail Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        user.updateUserEmail(email);
    }

    /**
     * 유저 비밀번호 변경
     * @param personalId
     * @param password
     */
    @Transactional
    public Response updateUserPassword(String personalId, String password, String newPassword) {
        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("updateUserPassword Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });
        if (!passwordEncoder.matches(password, user.getPassword())){
                log.error("updateUserPassword Exception : [현재 비밀번호 오류]", ExceptionCode.PASSWORD_ERROR);
                return Response.passwordError(ExceptionCode.PASSWORD_ERROR);
        }
        user.updateUserPassword(passwordEncoder.encode(newPassword));
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
            user.updateUserAuthType(auth.equals("ON") ? AuthType.ADMIN : AuthType.USER);
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
    public void deleteUser(String personalId) {
        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("deleteUser Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        // 유저 삭제
        try {
            userRepository.deleteById(user.getId());
        } catch (Exception e) {
            log.error("deleteUser Exception : {}", e.getMessage());
            throw new LibraryException.DataDeleteException(ExceptionCode.DATA_DELETE_EXCEPTION);
        }
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
}


