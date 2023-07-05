package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.model.dto.UserDto;
import hyundaiautoever.library.model.dto.request.UserRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // 조회에서 성능 최적화
public class UserService {

    private final UserRepository userRepository;

    /**
     * 로그인 login
     */
    @Transactional
    public Response loginUser(String personalId, String password) {
        Optional<User> optionalUser = userRepository.findByPersonalId(personalId);
        if (optionalUser.isEmpty()) {
            return Response.personlIdError(ExceptionCode.PERSONALID_ERROR);
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
                .password(request.getPassword())
                .email(request.getEmail())
                .partType(request.getPartType())
                .nickname(request.getNickname())
                .build();
        userRepository.save(user);
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
        if (!password.equals(user.getPassword())){
                log.error("updateUserPassword Exception : [현재 비밀번호 오류]", ExceptionCode.PASSWORD_ERROR);
                return Response.passwordError(ExceptionCode.PASSWORD_ERROR);
        }
        user.updateUserPassword(newPassword);
        return Response.ok();
    }
}


