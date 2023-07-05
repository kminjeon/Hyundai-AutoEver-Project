package hyundaiautoever.library.controller;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.model.dto.request.UserRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 로그인
     */
    @GetMapping("/login")
    public Response loginUser(@RequestParam("personalId") String personalId, @RequestParam("password") String password) {
        log.info("[loginUser]");
        return userService.loginUser(personalId, password);
    }


    /**
     * 회원가입
     * @return join
     */
    @PostMapping("/join") // localhost:8080/api/join
    public Response joinUser(@RequestBody @Valid UserRequest.CreateUserRequest request) {
        log.info("[createUser]");
        userService.createUser(request);
        return Response.ok();
    }

    /**
     * 사용자 프로필 조회
     */
    @GetMapping("/mypage/profile/{personalId}")
    public Response searchUserProfile(@PathVariable("personalId") String personalId) {
        return Response.ok().setData(userService.searchUserProfile(personalId));
    }

    /**
     * 닉네임 unique 체크
     */
    @GetMapping("/check/nickname/{nickname}")
    public Response checkNickname(@PathVariable("nickname") String nickname) {
        log.info("check nickname");
        if (userService.checkNickname(nickname)) {
            return Response.dataDuplicateException(ExceptionCode.DATA_DUPLICATE_EXCEPTION);
        }
        return Response.ok();
    }

    /**
     * 이메일 unique 체크
     */
    @GetMapping("/check/email/{email}")
    public Response checkEmail(@PathVariable("email") String email) {
        log.info("check email");
        if (userService.checkEmail(email)) {
            return Response.dataDuplicateException(ExceptionCode.DATA_DUPLICATE_EXCEPTION);
        }
        return Response.ok();
    }

    /**
     * 아이디 unique 체크
     */
    @GetMapping("/check/personalId/{personalId}")
    public Response checkPersonalId(@PathVariable("personalId") String personalId) {
        log.info("check personalId");
        if (userService.checkPersonalId(personalId)) {
            return Response.dataDuplicateException(ExceptionCode.DATA_DUPLICATE_EXCEPTION);
        }
        return Response.ok();
    }


    /**
     * 유저 이메일 수정
     * @param personalId
     * @param email
     * @return Response.ok
     */
    @PutMapping("/mypage/profile/email")
    public Response updateUserEmail(@RequestParam("personalId") String personalId, @RequestParam("email") String email) {
        userService.updateUserEmail(personalId, email);
        return Response.ok();
    }

    /**
     * 유저 비밀번호 수정
     * @param personalId
     * @param password
     * @return Response.ok
     */
    @PutMapping("/mypage/profile/password")
    public Response updateUserPassword(@RequestParam("personalId") String personalId,
                                       @RequestParam("password") String password,
                                       @RequestParam("newPassword") String newPassword) {
        return userService.updateUserPassword(personalId, password, newPassword);
    }
}
