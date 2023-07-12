package hyundaiautoever.library.controller;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.model.dto.UserDto;
import hyundaiautoever.library.model.dto.request.UserRequest;
import hyundaiautoever.library.model.dto.request.UserRequest.UpdateProfileRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.service.UserService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    @PostMapping("/login")
    public Response loginUser(@RequestBody @Valid UserRequest.LoginRequest request) {
        log.info("UserController : [loginUser]");
        return userService.loginUser(request);
    }


    /**
     * 회원가입
     * @return join
     */
    @PostMapping("/join") // localhost:8080/api/join
    public Response joinUser(@RequestBody @Valid UserRequest.CreateUserRequest request) {
        log.info("UserController : [createUser]");
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
        log.info("UserController : [checkNickname]");
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
        log.info("UserController: [checkEmail]");
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
        log.info("UserController : [checkPersonalId]");
        if (userService.checkPersonalId(personalId)) {
            return Response.dataDuplicateException(ExceptionCode.DATA_DUPLICATE_EXCEPTION);
        }
        return Response.ok();
    }


    /**
     * 유저 프로필 변경
     * @param request
     * @return ok
     */
    @PutMapping("/mypage/profile/update")
    public Response updateProfile(@RequestBody @Valid UpdateProfileRequest request) {
        log.info("UserController : [updateProfile]");
        return userService.updateProfile(request);
    }

    /**
     * ADMIN 권한 관리
     * @param personalId
     * @param auth
     * @return LoginDto
     */
    @PutMapping("/admin/auth/update")
    public Response updateAuth(@RequestParam("personalId") String personalId,
                               @RequestParam("auth") String auth) {
        log.info("UserController : [updateAuth]");
        return Response.ok().setData(userService.updateAuth(personalId, auth));
    }


    /**
     * 회원 탈퇴
     * @param personalId
     * @return
     */
    @DeleteMapping("/mypage/withdraw")
    public Response deleteUser(@RequestParam("personalId") String personalId) {
        log.info("UserController : [deleteUser]");
        userService.deleteUser(personalId);
        return Response.ok();
    }


    /**
     * 권한 관리 유저 페이지 조회
     * @param pageable
     * @param personalId
     * @param name
     * @return UserAuthPage
     */
    @GetMapping("/admin/auth/getPage")
    public Response searchUserAuthPage(@PageableDefault(size = 10) Pageable pageable,
                                   @RequestParam(required = false) String personalId,
                                   @RequestParam(required = false) String name) {
        log.info("UserController : [searchUserList]");
        return Response.ok().setData(userService.searchUserAuthPage(pageable, personalId, name));
    }


}
