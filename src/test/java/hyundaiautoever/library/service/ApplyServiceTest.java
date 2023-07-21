package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.common.type.PartType;
import hyundaiautoever.library.model.dto.ApplyDto;
import hyundaiautoever.library.model.dto.request.ApplyRequest;
import hyundaiautoever.library.model.dto.request.UserRequest;
import hyundaiautoever.library.model.entity.Apply;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.ApplyRepository;
import hyundaiautoever.library.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplyServiceTest {

    @InjectMocks
    private ApplyService applyService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplyRepository applyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void 도서_신청_생성_성공() throws Exception {
        //given
        User user = createUser();
        ApplyRequest.CreateApplyRequest request = createApplyRequest(user.getPersonalId());
        Apply apply = createApply(user);

        //mock
        given(applyRepository.save(any())).willReturn(apply);
        given(applyRepository.findById(any())).willReturn(Optional.ofNullable(apply));
        given(userRepository.findByPersonalId(any())).willReturn(Optional.ofNullable(user));

        //when
        Long applyId = applyService.createApply(request);

        //then
        Optional<Apply> applyOptional = applyRepository.findById(applyId);
        assertTrue(applyOptional.isPresent());
        assertEquals(user.getPersonalId(),applyOptional.get().getUser().getPersonalId());
    }

    @Test
    public void 도서_신청_수정() throws Exception {
        User user = createUser();
        Apply apply = createApply(user);

        ApplyRequest.UpdateApplyRequest request = updateApplyRequest();

        //mock
        when(applyRepository.findById(apply.getId())).thenReturn(Optional.of(apply));

        //when
        ApplyDto.UpdateApplyDto result = applyService.updateApply(apply.getId(), request);

        //then
        assertEquals(request.getTitle(), apply.getTitle());
        assertEquals(request.getIsbn(), apply.getIsbn());
        assertEquals(request.getAuthor(), apply.getAuthor());
        assertEquals(request.getPublisher(), apply.getPublisher());
    }

    @Test
    public void 도서_신청_수정_실패() throws Exception {
        Long applyId = 1L;
        ApplyRequest.UpdateApplyRequest request = updateApplyRequest();

        when(applyRepository.findById(applyId)).thenReturn(Optional.empty());

        //when
        LibraryException.DataNotFoundException exception = assertThrows(
                LibraryException.DataNotFoundException.class,
                () -> applyService.updateApply(applyId, request)
        );

        //then
        assertEquals(ExceptionCode.DATA_NOT_FOUND_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
        verify(applyRepository, times(1)).findById(applyId);
    }
    @Test
    public void 도서_신청_삭제_성공() throws Exception {
        User user = createUser();
        Apply apply = createApply(user);
        Long applyId = 1L;
        //when
        when(applyRepository.findById(applyId)).thenReturn(Optional.of(apply));
        assertDoesNotThrow(() -> applyService.deleteApply(applyId));

        //then
        verify(applyRepository, times(1)).deleteById(applyId);
    }

    @Test
    public void 도서_신청_삭제_apply_없음_실패() throws Exception {
        //given
        Long applyId = 1L;

        //mock
        when(applyRepository.findById(applyId)).thenReturn(Optional.empty());

        //when
        LibraryException.DataNotFoundException exception = assertThrows(
                LibraryException.DataNotFoundException.class,
                () -> applyService.deleteApply(applyId)
        );

        //then
        assertEquals(ExceptionCode.DATA_NOT_FOUND_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
    }

    @Test
    public void 도서_신청_삭제_오류_실패() {
        //then
        Long applyId = 1L;
        User user = createUser();
        Apply apply = createApply(user);

        //mock
        when(applyRepository.findById(applyId)).thenReturn(Optional.of(apply));
        doThrow(new RuntimeException("error")).when(applyRepository).deleteById(applyId);

        //when
        LibraryException.DataDeleteException exception = assertThrows(
                LibraryException.DataDeleteException.class,
                () -> applyService.deleteApply(applyId)
        );

        //then
        assertEquals(ExceptionCode.DATA_DELETE_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
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
        request.setIsbn("1232");
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
        ReflectionTestUtils.setField(user, "id", 1L);
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
        ReflectionTestUtils.setField(apply, "id", 1L);
        return apply;
    }

    private Apply createApply2(User user) {
        Apply apply = new Apply("도서신청2", "1233452", "작가2", "출판2", user);
        ReflectionTestUtils.setField(apply, "id", 1L);
        return apply;
    }

}