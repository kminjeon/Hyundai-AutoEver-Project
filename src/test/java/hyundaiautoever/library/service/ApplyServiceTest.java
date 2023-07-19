package hyundaiautoever.library.service;

import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.common.type.PartType;
import hyundaiautoever.library.model.dto.request.ApplyRequest;
import hyundaiautoever.library.model.dto.request.BookRequest;
import hyundaiautoever.library.model.dto.request.UserRequest;
import hyundaiautoever.library.model.entity.Apply;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.ApplyRepository;
import hyundaiautoever.library.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
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

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void 도서_신청() throws Exception {
        //Given
        User user = createUser();
        userRepository.save(user);
        ApplyRequest.CreateApplyRequest applyRequest = createApplyRequest(user.getPersonalId());

        //when
        Long applyId = applyService.createApply(applyRequest);
        Optional<Apply> applyOptional = applyRepository.findById(applyId);

        //then
        assertTrue(applyOptional.isPresent());
        assertEquals(user.getPersonalId(),applyOptional.get().getUser().getPersonalId());
    }

    @Test
    public void 도서_신청_수정() throws Exception {
        User user = createUser();
        userRepository.save(user);
        Apply apply = createApply(user);
        applyRepository.save(apply);

        ApplyRequest.UpdateApplyRequest request = updateApplyRequest();

        //when
        applyService.updateApply(apply.getId(), request);

        //then
        assertEquals(request.getTitle(), apply.getTitle());
        assertEquals(request.getIsbn(), apply.getIsbn());
        assertEquals(request.getAuthor(), apply.getAuthor());
        assertEquals(request.getPublisher(), apply.getPublisher());
    }

    @Test
    public void 도서_신청_삭제() throws Exception {
        User user = createUser();
        userRepository.save(user);
        Apply apply = createApply(user);
        applyRepository.save(apply);

        //when
        applyService.deleteApply(apply.getId());

        //then
        assertEquals(Optional.empty(), applyRepository.findById(apply.getId()));
    }

    private ApplyRequest.CreateApplyRequest createApplyRequest(String personalId) {
        ApplyRequest.CreateApplyRequest request = new ApplyRequest.CreateApplyRequest();
        request.setTitle("테스트 제목");
        request.setAuthor("테스트 작가");
        request.setIsbn("123");
        request.setPublisher("테스트 출판");
        request.setPersonalId(personalId);
        return request;
    }

    private ApplyRequest.UpdateApplyRequest updateApplyRequest() {
        ApplyRequest.UpdateApplyRequest request = new ApplyRequest.UpdateApplyRequest();
        request.setTitle("테스트 제목2");
        request.setAuthor("테스트 작가2");
        request.setIsbn("1233");
        request.setPublisher("테스트 출판2");
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

    private User createUser() {
        User user = new User("회원가입테스트","jointest", passwordEncoder.encode("test"),"test@join.com", PartType.MOBIS, "joinnickname");
        return user;
    }

    private User createUser2() {
        User user = new User("회원가입테스트","jointest2", passwordEncoder.encode("test"),"test@join.com2", PartType.MOBIS, "joinnickname2");
        return user;
    }

    private Book createBook() {
        Book book = new Book("테스트제목", "테스트작가", "123", "테스트출판", CategoryType.NOVEL, "테스트설명");
        return book;
    }

    private Apply createApply(User user) {
        Apply apply = new Apply("도서신청", "123345", "작가", "출판", user);
        return apply;
    }
}