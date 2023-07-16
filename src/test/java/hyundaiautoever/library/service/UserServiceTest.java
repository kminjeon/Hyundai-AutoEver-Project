package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.common.type.PartType;
import hyundaiautoever.library.model.dto.UserDto;
import hyundaiautoever.library.model.dto.request.UserRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class UserServiceTest {


    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void 회원가입() throws Exception {
        //Given
        UserRequest.CreateUserRequest request = createUserRequest();

        //when
        userService.createUser(request);
        User user = userRepository.findByEmail(request.getEmail());

        //then
        assertNotNull(user, "사용자가 정상적으로 생성되었음");
        assertEquals(request.getEmail(), user.getEmail(), "회원가입 이메일 동일");
    }


    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        UserRequest.CreateUserRequest request = createUserRequest();
        userService.createUser(request);

        UserRequest.CreateUserRequest request2 = createUserRequest();

        // When
        LibraryException.DataDuplicateException exception = assertThrows(
                LibraryException.DataDuplicateException.class,
                () -> userService.createUser(request2)
        );

        //then
        assertEquals(ExceptionCode.DATA_DUPLICATE_EXCEPTION.getMessage(), exception.getExceptionCode().getMessage(), "중복 회원 예외 메시지 일치");
    }

    @Test
    public void 로그인() {
        // Given
        User user = createUser();
        userRepository.save(user);

        UserRequest.LoginRequest request = new UserRequest.LoginRequest();
        request.setPersonalId(user.getPersonalId());
        request.setPassword("test");

        // When
        Response response = userService.loginUser(request);

        // Then
        assertEquals(HttpStatus.OK.value(), response.getCode(), "응답 상태 코드 일치");
        UserDto.LoginDto loginDto = (UserDto.LoginDto) response.getData();
        assertNotNull(loginDto, "응답 데이터가 null이 아님");
        assertEquals(user.getId(), loginDto.getId(), "사용자 ID 일치");
    }


    private UserRequest.CreateUserRequest createUserRequest() {
        UserRequest.CreateUserRequest request = new UserRequest.CreateUserRequest();
        request.setEmail("test@join.com");
        request.setPartType(PartType.MOBIS);
        request.setName("회원가입테스트");
        request.setNickname("joinnickname");
        request.setPassword("test");
        request.setPersonalId("jointest");
        return request;
    }

    private User createUser() {
        User user = new User("회원가입테스트","jointest", passwordEncoder.encode("test"),"test@join.com", PartType.MOBIS, "joinnickname");
        return user;
    }
}