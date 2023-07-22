package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.common.type.PartType;
import hyundaiautoever.library.model.dto.request.BookRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Rent;
import hyundaiautoever.library.model.entity.Reserve;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.BookRepository;
import hyundaiautoever.library.repository.RentRepository;
import hyundaiautoever.library.repository.ReserveRepository;
import hyundaiautoever.library.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Repeat;
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
class ReserveServiceTest {

    @InjectMocks
    ReserveService reserveService;

    @Mock
    ReserveRepository reserveRepository;

    @Mock
    RentRepository rentRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    public void 예약_생성_성공() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();

        //mock
        given(userRepository.findByPersonalId(any())).willReturn(Optional.ofNullable(user));
        given(bookRepository.findById(any())).willReturn(Optional.ofNullable(book));
        given(rentRepository.findByUserAndBook(user, book)).willReturn(null);
        given(reserveRepository.findByUserAndBook(user, book)).willReturn(Optional.empty());


        //when & then
        assertDoesNotThrow(() -> reserveService.createReserve(user.getPersonalId(), book.getId()));
    }

    @Test
    public void 예약_생성_존재하지_않는_personalId_예외() throws Exception {
        //given
        Long bookId = 1L;
        String nonPersonalId = "non";

        //mock
        given(userRepository.findByPersonalId(any())).willReturn(Optional.empty());

        //when
        LibraryException.DataNotFoundException exception = assertThrows(
                LibraryException.DataNotFoundException.class,
                () -> reserveService.createReserve(nonPersonalId, bookId)
        );

        //then
        assertEquals(ExceptionCode.DATA_NOT_FOUND_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
        verify(userRepository, times(1)).findByPersonalId(nonPersonalId);
    }

    @Test
    public void 예약_생성_존재하지_않는_bookId_예외() throws Exception {
        // Given
        User user = createUser();
        Long nonBookId = 1L;

        //mock
        given(userRepository.findByPersonalId(any())).willReturn(Optional.ofNullable(user));
        given(bookRepository.findById(any())).willReturn(Optional.empty());

        //when
        LibraryException.DataNotFoundException exception = assertThrows(
                LibraryException.DataNotFoundException.class,
                () -> reserveService.createReserve(user.getPersonalId(), nonBookId)
        );

        //then
        assertEquals(ExceptionCode.DATA_NOT_FOUND_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
        verify(userRepository, times(1)).findByPersonalId(user.getPersonalId());
        verify(bookRepository, times(1)).findById(nonBookId);
    }

    @Test
    public void 예약_생성_대여중_예외() throws Exception {
        // Given
        User user = createUser();
        Book book = createBook();
        Rent rent = createRent(user, book);

        //mock
        given(userRepository.findByPersonalId(any())).willReturn(Optional.ofNullable(user));
        given(bookRepository.findById(any())).willReturn(Optional.ofNullable(book));
        given(rentRepository.findByUserAndBook(any(), any())).willReturn(rent);

        //when
        LibraryException.AlreadyRentException exception = assertThrows(
                LibraryException.AlreadyRentException.class,
                () -> reserveService.createReserve(user.getPersonalId(), book.getId())
        );

        //then
        assertEquals(ExceptionCode.ALREADY_RENT_ERROR.getCode(), exception.getExceptionCode().getCode());
        verify(userRepository, times(1)).findByPersonalId(user.getPersonalId());
        verify(bookRepository, times(1)).findById(book.getId());
        verify(rentRepository, times(1)).findByUserAndBook(user, book);
    }

    @Test
    public void 예약_생성_중복_예약_예외() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        Reserve reserve = createReserve(user, book);

        //mock
        given(userRepository.findByPersonalId(any())).willReturn(Optional.ofNullable(user));
        given(bookRepository.findById(any())).willReturn(Optional.ofNullable(book));
        given(rentRepository.findByUserAndBook(any(), any())).willReturn(null);
        given(reserveRepository.findByUserAndBook(any(), any())).willReturn(Optional.ofNullable(reserve));

        //when
        LibraryException.DataDuplicateException exception = assertThrows(
                LibraryException.DataDuplicateException.class,
                () -> reserveService.createReserve(user.getPersonalId(), book.getId())
        );

        //then
        assertEquals(ExceptionCode.DATA_DUPLICATE_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
        verify(userRepository, times(1)).findByPersonalId(user.getPersonalId());
        verify(bookRepository, times(1)).findById(book.getId());
        verify(rentRepository, times(1)).findByUserAndBook(user, book);
        verify(reserveRepository, times(1)).findByUserAndBook(user, book);
    }

    @Test
    public void 예약_생성_SAVE_실패() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();

        //mock
        given(userRepository.findByPersonalId(any())).willReturn(Optional.ofNullable(user));
        given(bookRepository.findById(any())).willReturn(Optional.ofNullable(book));
        given(rentRepository.findByUserAndBook(any(), any())).willReturn(null);
        given(reserveRepository.findByUserAndBook(any(), any())).willReturn(Optional.empty());
        doThrow(new RuntimeException("error")).when(reserveRepository).save(any());

        //when
        LibraryException.DataSaveException exception = assertThrows(
                LibraryException.DataSaveException.class,
                () -> reserveService.createReserve(user.getPersonalId(), book.getId())
        );

        //then
        assertEquals(ExceptionCode.DATA_SAVE_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
        verify(userRepository, times(1)).findByPersonalId(user.getPersonalId());
        verify(bookRepository, times(1)).findById(book.getId());
        verify(rentRepository, times(1)).findByUserAndBook(user, book);
        verify(reserveRepository, times(1)).findByUserAndBook(user, book);
    }

    @Test
    public void 예약_삭제_다음_예약x_성공() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        Reserve reserve = createReserve(user, book);
        List<Reserve> reserveList = new ArrayList<>();

        //mock
        given(reserveRepository.findById(any())).willReturn(Optional.ofNullable(reserve));
        given(reserveRepository.findReserveListByWaitNumberAndBook(any(), any())).willReturn(reserveList);

        //when & then
        assertDoesNotThrow(() -> reserveService.deleteReserve(reserve.getId()));
    }

    @Test
    public void 예약_삭제_다음_예약o_성공() throws Exception {
        //given
        User user = createUser();
        User user2 = createUser2();
        Book book = createBook();
        Reserve reserve = createReserve(user, book);
        Reserve reserve2 = createReserve2(user2, book);

        List<Reserve> reserveList = new ArrayList<>();
        reserveList.add(reserve2);

        //mock
        given(reserveRepository.findById(any())).willReturn(Optional.ofNullable(reserve));
        given(reserveRepository.findReserveListByWaitNumberAndBook(any(), any())).willReturn(reserveList);

        //when
        assertDoesNotThrow(() -> reserveService.deleteReserve(reserve.getId()));

        //then
        assertEquals(reserve2.getWaitNumber(), 1);
        verify(reserveRepository, times(1)).findById(reserve.getId());
        verify(reserveRepository, times(1)).findReserveListByWaitNumberAndBook(reserve.getWaitNumber(), book);
    }

    @Test
    public void 예약_삭제_NOTFOUND_예외() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        Long reserveId = 1L;

        //mock
        given(reserveRepository.findById(any())).willReturn(Optional.empty());

        //when
        LibraryException.DataNotFoundException exception = assertThrows(
                LibraryException.DataNotFoundException.class,
                () -> reserveService.deleteReserve(reserveId)
        );

        //then
        assertEquals(ExceptionCode.DATA_NOT_FOUND_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
        verify(reserveRepository, times(1)).findById(reserveId);
    }

    @Test
    public void 예약_삭제_delete_예외() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        Reserve reserve = createReserve(user, book);
        List<Reserve> reserveList = new ArrayList<>();

        //mock
        given(reserveRepository.findById(any())).willReturn(Optional.ofNullable(reserve));
        given(reserveRepository.findReserveListByWaitNumberAndBook(any(), any())).willReturn(reserveList);
        doThrow(new RuntimeException("error")).when(reserveRepository).deleteById(any());

        //when
        LibraryException.DataDeleteException exception = assertThrows(
                LibraryException.DataDeleteException.class,
                () -> reserveService.deleteReserve(reserve.getId())
        );

        //then
        assertEquals(ExceptionCode.DATA_DELETE_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
        verify(reserveRepository, times(1)).findById(reserve.getId());
        verify(reserveRepository, times(1)).findReserveListByWaitNumberAndBook(reserve.getWaitNumber(), book);
    }

    @Test
    public void 대여가능예약_대여_이후예약x_성공() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        Reserve reserve = createReserve(user, book);
        List<Reserve> reserveList = new ArrayList<>();

        //mock
        given(reserveRepository.findById(any())).willReturn(Optional.ofNullable(reserve));
        given(reserveRepository.findReserveListByWaitNumberAndBook(any(), any())).willReturn(reserveList);


        //when
        assertDoesNotThrow(() -> reserveService.reserveRent(reserve.getId()));

        //then
        assertEquals(1, book.getRentCount());
        assertEquals(1, user.getRentCount());
    }

    @Test
    public void 대여가능예약_대여_이후예약o_성공() throws Exception {
        //given
        User user = createUser();
        User user2 = createUser2();
        Book book = createBook();
        Reserve reserve = createReserve(user, book);
        Reserve reserve2 = createReserve2(user2, book);
        List<Reserve> reserveList = new ArrayList<>();
        reserveList.add(reserve2);

        //mock
        given(reserveRepository.findById(any())).willReturn(Optional.ofNullable(reserve));
        given(reserveRepository.findReserveListByWaitNumberAndBook(any(), any())).willReturn(reserveList);


        //when
        assertDoesNotThrow(() -> reserveService.reserveRent(reserve.getId()));

        //then
        assertEquals(1, book.getRentCount());
        assertEquals(1, user.getRentCount());
        assertEquals(1, reserve2.getWaitNumber());
    }

    @Test
    public void 대여가능예약_대여_NOTFOUND_예외() throws Exception {
        //given
       Long reserveId = 1L;

        //mock
        given(reserveRepository.findById(any())).willReturn(Optional.empty());


        //when
        LibraryException.DataNotFoundException exception = assertThrows(
                LibraryException.DataNotFoundException.class,
                () -> reserveService.reserveRent(reserveId)
        );

        //then
        assertEquals(ExceptionCode.DATA_NOT_FOUND_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
    }

    @Test
    public void 대여가능예약_대여_대여횟수초과_예외() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        Reserve reserve = createReserve(user, book);
        List<Reserve> reserveList = new ArrayList<>();
        user.updateUserRentCount(3);

        //mock
        given(reserveRepository.findById(any())).willReturn(Optional.ofNullable(reserve));

        //when
        LibraryException.MaxRentException exception = assertThrows(
                LibraryException.MaxRentException.class,
                () -> reserveService.reserveRent(reserve.getId())
        );

        //then
        assertEquals(ExceptionCode.MAX_RENT_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
    }

    @Test
    public void 대여가능예약_대여_SAVE_예외() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        Reserve reserve = createReserve(user, book);
        List<Reserve> reserveList = new ArrayList<>();

        //mock
        given(reserveRepository.findById(any())).willReturn(Optional.ofNullable(reserve));
        doThrow(new RuntimeException("error")).when(rentRepository).save(any());

        //when
        LibraryException.DataSaveException exception = assertThrows(
                LibraryException.DataSaveException.class,
                () -> reserveService.reserveRent(reserve.getId())
        );

        //then
        assertEquals(ExceptionCode.DATA_SAVE_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
    }

    @Test
    public void 대여가능예약_대여_예약DELETE_예외() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        Reserve reserve = createReserve(user, book);
        List<Reserve> reserveList = new ArrayList<>();

        //mock
        given(reserveRepository.findById(any())).willReturn(Optional.ofNullable(reserve));
        given(reserveRepository.findReserveListByWaitNumberAndBook(any(), any())).willReturn(reserveList);
        doThrow(new RuntimeException("error")).when(reserveRepository).deleteById(any());

        //when
        LibraryException.DataDeleteException exception = assertThrows(
                LibraryException.DataDeleteException.class,
                () -> reserveService.reserveRent(reserve.getId())
        );

        //then
        assertEquals(ExceptionCode.DATA_DELETE_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
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

    private Reserve createReserve(User user, Book book) {
        Reserve reserve = new Reserve(user, book, 1);
        ReflectionTestUtils.setField(reserve, "id", 1L);
        return reserve;
    }

    private Reserve createReserve2(User user, Book book) {
        Reserve reserve = new Reserve(user, book, 2);
        ReflectionTestUtils.setField(reserve, "id", 2L);
        return reserve;
    }

    private Rent createRent(User user, Book book) {
        Rent rent = new Rent(user, book);
        ReflectionTestUtils.setField(rent, "id", 1L);
        return rent;
    }

}