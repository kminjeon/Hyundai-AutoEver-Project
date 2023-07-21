package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.common.type.PartType;
import hyundaiautoever.library.model.dto.request.BookRequest;
import hyundaiautoever.library.model.dto.request.UserRequest;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Love;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.BookRepository;
import hyundaiautoever.library.repository.LoveRepository;
import hyundaiautoever.library.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoveServiceTest {

    @InjectMocks
    LoveService loveService;

    @Mock
    UserRepository userRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    LoveRepository loveRepository;

    @Mock
    PasswordEncoder passwordEncoder;


    @Test
    public void 좋아요_생성() {
        //given
        User user = createUser();
        Book book = createBook();
        Love love = createLove(user, book);
        Integer count = book.getLoveCount();

        //mock
        given(userRepository.findByPersonalId(user.getPersonalId())).willReturn(Optional.ofNullable(user));
        given(bookRepository.findById(book.getId())).willReturn(Optional.ofNullable(book));
        given(loveRepository.findByUserAndBook(user, book)).willReturn(Optional.empty());
        given(loveRepository.findById(any())).willReturn(Optional.ofNullable(love));

        //when
        Long loveId = loveService.createLove(user.getPersonalId(), book.getId());
        Optional<Love> loveOptional = loveRepository.findById(loveId);

        //then
        assertTrue(loveOptional.isPresent(), "좋아요가 정상적으로 생성되었음");
        Love getLove = loveOptional.get();
        assertEquals(book.getId(), getLove.getBook().getId(), "도서 ID 일치");
        assertEquals(user.getId(), getLove.getUser().getId(), "사용자 ID 일치");
        assertEquals(count + 1, bookRepository.findById(book.getId()).get().getLoveCount(), "도서 좋아요 수 증가");
    }

    @Test
    public void 좋아요_생성_NOT_FOUNT_USER() {
        //given
        String personalId = "test";
        Long bookId = 1L;

        //mock
        given(userRepository.findByPersonalId(personalId)).willReturn(Optional.empty());

        //when
        LibraryException.DataNotFoundException exception = assertThrows(
                LibraryException.DataNotFoundException.class,
                () -> loveService.createLove(personalId, bookId)
        );

        //then
        assertEquals(ExceptionCode.DATA_NOT_FOUND_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
    }

    @Test
    public void 좋아요_생성_NOT_FOUNT_BOOK() {
        //given
        User user = createUser();
        Long bookId = 1L;

        //mock
        given(userRepository.findByPersonalId(user.getPersonalId())).willReturn(Optional.ofNullable(user));
        given(bookRepository.findById(bookId)).willReturn(Optional.empty());

        //when
        LibraryException.DataNotFoundException exception = assertThrows(
                LibraryException.DataNotFoundException.class,
                () -> loveService.createLove(user.getPersonalId(), bookId)
        );

        //then
        assertEquals(ExceptionCode.DATA_NOT_FOUND_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
        verify(userRepository, times(1)).findByPersonalId(user.getPersonalId());
    }

    @Test
    public void 좋아요_생성_Data_Duplicate() {
        //given
        User user = createUser();
        Book book = createBook();

        Love love = createLove(user, book);

        //mock
        given(userRepository.findByPersonalId(user.getPersonalId())).willReturn(Optional.ofNullable(user));
        given(bookRepository.findById(book.getId())).willReturn(Optional.ofNullable(book));
        given(loveRepository.findByUserAndBook(user, book)).willReturn(Optional.ofNullable(love));

        //when
        LibraryException.DataDuplicateException exception = assertThrows(
                LibraryException.DataDuplicateException.class,
                () -> loveService.createLove(user.getPersonalId(), book.getId())
        );

        //then
        assertEquals(ExceptionCode.DATA_DUPLICATE_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
        verify(userRepository, times(1)).findByPersonalId(user.getPersonalId());
        verify(bookRepository, times(1)).findById(book.getId());
}

    @Test
    public void 좋아요_생성_SAVE_EXCEPTION() {
        User user = createUser();
        Book book = createBook();

        //mock
        given(userRepository.findByPersonalId(user.getPersonalId())).willReturn(Optional.ofNullable(user));
        given(bookRepository.findById(book.getId())).willReturn(Optional.ofNullable(book));
        given(loveRepository.findByUserAndBook(user, book)).willReturn(Optional.empty());
        doThrow(new RuntimeException("error")).when(loveRepository).save(any());

        //when
        LibraryException.DataSaveException exception = assertThrows(
                LibraryException.DataSaveException.class,
                () -> loveService.createLove(user.getPersonalId(), book.getId())
        );

        //then
        assertEquals(ExceptionCode.DATA_SAVE_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
        verify(userRepository, times(1)).findByPersonalId(user.getPersonalId());
        verify(bookRepository, times(1)).findById(book.getId());
        verify(loveRepository, times(1)).findByUserAndBook(user, book);

    }

    @Test
    public void 좋아요_삭제_성공() {
        User user = createUser();
        Book book = createBook();
        Love love = createLove(user, book);
        book.updateLoveCount(book.getLoveCount() + 1);

        //mock
        given(loveRepository.findById(love.getId())).willReturn(Optional.ofNullable(love));
        doNothing().when(loveRepository).deleteById(love.getId());

        //when
        assertDoesNotThrow(() -> loveService.deleteLove(love.getId()));

        //then
        assertEquals(0, book.getLoveCount());
    }

    @Test
    public void 좋아요_삭제_NOT_FOUNT_실패() {
        Long loveId = 1L;

        //mock
        given(loveRepository.findById(loveId)).willReturn(Optional.empty());

        //when
        LibraryException.DataNotFoundException exception = assertThrows(
                LibraryException.DataNotFoundException.class,
                () -> loveService.deleteLove(loveId)
        );

        //then
        assertEquals(ExceptionCode.DATA_NOT_FOUND_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
        verify(loveRepository, times(1)).findById(loveId);
    }

    @Test
    public void 좋아요_삭제_DELETE_EXCEPTION_실패() {
        User user = createUser();
        Book book = createBook();
        Love love = createLove(user, book);

        //mock
        given(loveRepository.findById(love.getId())).willReturn(Optional.ofNullable(love));
        doThrow(new RuntimeException("error")).when(loveRepository).deleteById(love.getId());

        //when
        LibraryException.DataDeleteException exception = assertThrows(
                LibraryException.DataDeleteException.class,
                () -> loveService.deleteLove(love.getId())
        );

        //then
        assertEquals(ExceptionCode.DATA_DELETE_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
        verify(loveRepository, times(1)).findById(love.getId());
        verify(loveRepository, times(1)).deleteById(love.getId());
    }


    private User createUser() {
        User user = new User("회원가입테스트","jointest", passwordEncoder.encode("test"),"test@join.com", PartType.MOBIS, "joinnickname");
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private Book createBook() {
        Book book = new Book("테스트제목", "테스트작가", "123", "테스트출판", CategoryType.NOVEL, "테스트설명");
        ReflectionTestUtils.setField(book, "id", 1L);
        return book;
    }


    private Love createLove(User user, Book book) {
        Love love = new Love(user, book);
        ReflectionTestUtils.setField(love, "id", 1L);
        return love;
    }
}