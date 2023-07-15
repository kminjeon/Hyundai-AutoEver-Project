package hyundaiautoever.library.service;

import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.common.type.PartType;
import hyundaiautoever.library.model.dto.request.ApplyRequest;
import hyundaiautoever.library.model.dto.request.BookRequest;
import hyundaiautoever.library.model.dto.request.UserRequest;
import hyundaiautoever.library.model.entity.Apply;
import hyundaiautoever.library.repository.ApplyRepository;
import hyundaiautoever.library.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ApplyServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ApplyService applyService;

    @Autowired
    ApplyRepository applyRepository;

    @Test
    public void 도서_신청() throws Exception {
        //Given
        UserRequest.CreateUserRequest userRequest = createUserRequest();
        userService.createUser(userRequest);
        ApplyRequest.CreateApplyRequest applyRequest = createApplyRequest(userRequest.getPersonalId());

        //when
        Long applyId = applyService.createApply(applyRequest);
        Optional<Apply> applyOptional = applyRepository.findById(applyId);

        //then
        assertTrue(applyOptional.isPresent());
        assertEquals(userRequest.getPersonalId(),applyOptional.get().getUser().getPersonalId());
    }


    private ApplyRequest.CreateApplyRequest createApplyRequest(String personalId) {
        ApplyRequest.CreateApplyRequest request = new ApplyRequest.CreateApplyRequest();
        request.setTitle("테스트 제목2");
        request.setAuthor("테스트 작가2");
        request.setIsbn("1233");
        request.setPublisher("테스트 출판2");
        request.setPersonalId(personalId);
        return request;
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
}