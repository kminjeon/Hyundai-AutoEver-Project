package hyundaiautoever.library.service;

import hyundaiautoever.library.common.type.PartType;
import hyundaiautoever.library.model.dto.request.UserRequest;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
public class UserServiceTest {


    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @Test
    public void 회원가입() throws Exception {
        //Given
        UserRequest.CreateUserRequest request = new UserRequest.CreateUserRequest();

        request.setEmail("test@join.com");
        request.setPartType(PartType.MOBIS);
        request.setName("회원가입테스트");
        request.setNickname("joinnickname");
        request.setPassword("test");
        request.setPersonalId("jointest");

        //when
        userService.createUser(request);
        User user = userRepository.findByEmail(request.getEmail());

        //then
        assertEquals(user.getEmail(), request.getEmail(), "회원가입 이메일");

    }



}