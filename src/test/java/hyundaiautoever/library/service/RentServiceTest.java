package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.common.type.PartType;
import hyundaiautoever.library.common.type.RentType;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Rent;
import hyundaiautoever.library.model.entity.Reserve;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.BookRepository;
import hyundaiautoever.library.repository.RentRepository;
import hyundaiautoever.library.repository.ReserveRepository;
import hyundaiautoever.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentServiceTest {

    @InjectMocks
    RentService rentService;
    @Mock
    BookRepository bookRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    RentRepository rentRepository;
    @Mock
    ReserveRepository reserveRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    EmailService emailService;


    @Test
    public void 도서대여() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();

        //mock
        given(userRepository.findByPersonalId(any())).willReturn(Optional.ofNullable(user));
        given(bookRepository.findById(any())).willReturn(Optional.ofNullable(book));

        //when
        assertDoesNotThrow(() -> rentService.createRent(user.getPersonalId(), book.getId()));

        //then
        assertEquals(RentType.CLOSE, book.getRentType(), "도서 대여시 도서 상태는 CLOSE");
        assertEquals(1, book.getRentCount(), "도서 대여시 book rentCount + 1");
        assertEquals(1, user.getRentCount(), "도서 대여시 user rentCount + 1");
    }

    @Test
    public void 도서대여_최대_대여_3회_초과_실패() throws Exception {
        //given
        User user = createUser();
        Long bookId = 1L;

        user.updateUserRentCount(3);

        //mock
        given(userRepository.findByPersonalId(any())).willReturn(Optional.ofNullable(user));

        // when
        LibraryException.MaxRentException exception = assertThrows(
                LibraryException.MaxRentException.class,
                () -> rentService.createRent(user.getPersonalId(), bookId)
        );

        //then
        assertEquals(ExceptionCode.MAX_RENT_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
        verify(userRepository, times(1)).findByPersonalId(user.getPersonalId());
    }

    @Test
    public void 도서대여_대여중도서_실패() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        book.updateRentType(RentType.CLOSE);

        //mock
        given(userRepository.findByPersonalId(any())).willReturn(Optional.ofNullable(user));
        given(bookRepository.findById(any())).willReturn(Optional.ofNullable(book));


        // when
        LibraryException.RentStateException exception = assertThrows(
                LibraryException.RentStateException.class,
                () -> rentService.createRent(user.getPersonalId(), book.getId())
        );

        //then
        assertEquals(ExceptionCode.RENT_STATE_ERROR.getCode(), exception.getExceptionCode().getCode());
        verify(userRepository, times(1)).findByPersonalId(user.getPersonalId());
        verify(bookRepository, times(1)).findById(book.getId());
    }


    @Test
    public void 도서대여_SAVE_실패() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();

        //mock
        given(userRepository.findByPersonalId(any())).willReturn(Optional.ofNullable(user));
        given(bookRepository.findById(any())).willReturn(Optional.ofNullable(book));
        doThrow(new RuntimeException("error")).when(rentRepository).save(any());

        // when
        LibraryException.DataSaveException exception = assertThrows(
                LibraryException.DataSaveException.class,
                () -> rentService.createRent(user.getPersonalId(), book.getId())
        );

        //then
        assertEquals(ExceptionCode.DATA_SAVE_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
        verify(userRepository, times(1)).findByPersonalId(user.getPersonalId());
        verify(bookRepository, times(1)).findById(book.getId());
    }

    @Test
    public void 대출_연장_성공() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        Rent rent = createRent(user, book);

        //mock
        given(rentRepository.findById(any())).willReturn(Optional.ofNullable(rent));

        //when
        assertDoesNotThrow(() -> rentService.extentRent(rent.getId()));

        //then
        assertEquals(1, rent.getExtensionNumber());
        verify(rentRepository, times(1)).findById(rent.getId());
    }

    @Test
    public void 대출_연장_NOT_FOUND_실패() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        Rent rent = createRent(user, book);

        //mock
        given(rentRepository.findById(any())).willReturn(Optional.empty());

        // when
        LibraryException.DataNotFoundException exception = assertThrows(
                LibraryException.DataNotFoundException.class,
                () -> rentService.extentRent(rent.getId())
        );

        //then
        assertEquals(ExceptionCode.DATA_NOT_FOUND_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
        verify(rentRepository, times(1)).findById(rent.getId());
    }

    @Test
    public void 대출_연장_실패() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        Rent rent = createRent(user, book);
        rent.updateExtensionNumber(1);

        //mock
        given(rentRepository.findById(any())).willReturn(Optional.ofNullable(rent));

        // when
        LibraryException.RentExtensionException exception = assertThrows(
                LibraryException.RentExtensionException.class,
                () -> rentService.extentRent(rent.getId())
        );

        //then
        assertEquals(ExceptionCode.RENT_EXTENSION_ERROR.getCode(), exception.getExceptionCode().getCode());
        verify(rentRepository, times(1)).findById(rent.getId());
    }

    @Test
    public void 대여도서_반납_성공_예약x() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        Rent rent = createRent(user, book);
        user.updateUserRentCount(1);
        book.updateRentType(RentType.CLOSE);

        //mock
        given(rentRepository.findById(any())).willReturn(Optional.ofNullable(rent));
        given(reserveRepository.findByWaitNumberAndBook(1, book)).willReturn(null);

        //when
        assertDoesNotThrow(() -> rentService.returnBook(rent.getId()));

        //then
        assertEquals(RentType.OPEN, book.getRentType(), "도서 반납시 대여 가능 상태");
        assertNotNull(rent.getReturnDate(), "도서 반납시 returnDate가 null이 아님");
        assertEquals(0, user.getRentCount());
    }


    @Test
    public void 대여도서_반납_성공_예약o() throws Exception {
        //given
        User user = createUser();
        User user2= createUser2();
        Book book = createBook();
        Rent rent = createRent(user, book);
        user.updateUserRentCount(1);
        book.updateRentType(RentType.CLOSE);
        Reserve reserve = createReserve(user2, book);

        List<Reserve> reserveList = new ArrayList<>();
        reserveList.add(reserve);

        //mock
        given(rentRepository.findById(any())).willReturn(Optional.ofNullable(rent));
        given(reserveRepository.findByWaitNumberAndBook(1, book)).willReturn(reserve);
        given(reserveRepository.findAllByBook(book)).willReturn(reserveList);
        doNothing().when(emailService).sendRentEmail(any(), any());
        //when
        assertDoesNotThrow(() -> rentService.returnBook(rent.getId()));

        //then
        assertEquals(RentType.CLOSE, book.getRentType());
        assertNotNull(rent.getReturnDate(), "도서 반납시 returnDate가 null이 아님");
        assertEquals(0, user.getRentCount());
        assertEquals(0, reserve.getWaitNumber());
    }

    @Test
    public void 대여도서_반납_NOT_FOUNT_실패() throws Exception {
        //given
        Long rentId = 1L;

        //mock
        given(rentRepository.findById(any())).willReturn(Optional.empty());

        // when
        LibraryException.DataNotFoundException exception = assertThrows(
                LibraryException.DataNotFoundException.class,
                () -> rentService.extentRent(rentId)
        );

        //then
        assertEquals(ExceptionCode.DATA_NOT_FOUND_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
        verify(rentRepository, times(1)).findById(rentId);
    }

    private User createUser() {
        User user = new User("회원가입테스트","jointest", passwordEncoder.encode("test"),"test@join.com", PartType.MOBIS, "joinnickname");
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private User createUser2() {
        User user = new User("회원가입테스트","jointest2", passwordEncoder.encode("test"),"test@join.com2", PartType.MOBIS, "joinnickname2");
        ReflectionTestUtils.setField(user, "id", 2L);
        return user;
    }

    private Book createBook() {
        Book book = new Book("테스트제목", "테스트작가", "123", "테스트출판", CategoryType.NOVEL, "테스트설명");
        ReflectionTestUtils.setField(book, "id", 1L);
        return book;
    }

    private Rent createRent(User user, Book book) {
        Rent rent = new Rent(user, book);
        ReflectionTestUtils.setField(rent, "id", 1L);
        return rent;
    }

    private Reserve createReserve(User user, Book book) {
        Reserve reserve = new Reserve(user, book, 1);
        ReflectionTestUtils.setField(reserve, "id", 1L);
        return reserve;
    }
}