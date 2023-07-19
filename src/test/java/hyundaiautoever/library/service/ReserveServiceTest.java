package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.common.type.PartType;
import hyundaiautoever.library.model.dto.request.BookRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Reserve;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.BookRepository;
import hyundaiautoever.library.repository.ReserveRepository;
import hyundaiautoever.library.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Repeat;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest
class ReserveServiceTest {

    @Autowired
    ReserveService reserveService;

    @Autowired
    ReserveRepository reserveRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void 예약_생성() throws Exception {
        // Given
        User user = createUser();
        Book book = createBook();
        userRepository.save(user);
        bookRepository.save(book);

        // When
        Response response = reserveService.createReserve(user.getPersonalId(), book.getId());
        Long reserveId = (Long) response.getData();
        Optional<Reserve> optionalReserve = reserveRepository.findById(reserveId);

        // Then
        assertTrue(optionalReserve.isPresent(), "예약이 정상적으로 생성되었음");
        Reserve reserve = optionalReserve.get();
        assertAll("예약 정보 검증",
                () -> assertEquals(book, reserve.getBook(), "예약 도서 일치"),
                () -> assertEquals(user, reserve.getUser(), "예약 사용자 일치"),
                () -> assertEquals(1, reserve.getWaitNumber(), "대기 번호 일치")
        );
    }

    @Test
    public void 예약_생성_존재하지_않는_personalId_예외() throws Exception {
        // Given
        Book book = createBook();
        bookRepository.save(book);

        Long bookId = book.getId();
        String nonPersonalId = "non";

        // When & Then
        assertThrows(LibraryException.DataNotFoundException.class, () -> {
            reserveService.createReserve(nonPersonalId, bookId);
        });
    }

    @Test
    public void 예약_생성_존재하지_않는_bookId_예외() throws Exception {
        // Given
        User user = createUser();
        userRepository.save(user);

        String personalId = user.getPersonalId();
        Long nonExistingBookId = 0L;

        // When & Then
        assertThrows(LibraryException.DataNotFoundException.class, () -> {
            reserveService.createReserve(personalId, nonExistingBookId);
        });
    }

    @Test
    public void 예약_생성_중복_예약_예외() throws Exception {
        // Given
        User user = createUser();
        Book book = createBook();
        userRepository.save(user);
        bookRepository.save(book);

        reserveService.createReserve(user.getPersonalId(), book.getId()); // 이미 예약된 상태로 설정

        // When & Then
        assertThrows(LibraryException.DataDuplicateException.class, () -> {
            reserveService.createReserve(user.getPersonalId(), book.getId());
        });
    }

    @Test
    public void 예약_삭제() throws  Exception {
        // Given
        User user = createUser();
        Book book = createBook();
        userRepository.save(user);
        bookRepository.save(book);

        Reserve reserve = createReserve(user, book);
        reserveRepository.save(reserve);

        //when
        reserveRepository.deleteById(reserve.getId());
        assertEquals(Optional.empty(), reserveRepository.findById(reserve.getId()));
    }

    private User createUser() {
        User user = new User("회원가입테스트","jointest", passwordEncoder.encode("test"),"test@join.com", PartType.MOBIS, "joinnickname");
        return user;
    }

    private Book createBook() {
        Book book = new Book("테스트제목", "테스트작가", "123", "테스트출판", CategoryType.NOVEL, "테스트설명");
        return book;
    }

    private Reserve createReserve(User user, Book book) {
        Reserve reserve = new Reserve(user, book, 0);
        return reserve;
    }

}