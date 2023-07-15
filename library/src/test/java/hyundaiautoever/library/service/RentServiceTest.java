package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.common.type.PartType;
import hyundaiautoever.library.common.type.RentType;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Rent;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.BookRepository;
import hyundaiautoever.library.repository.RentRepository;
import hyundaiautoever.library.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class RentServiceTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RentRepository rentRepository;

    @Autowired
    RentService rentService;
    @Autowired
    PasswordEncoder passwordEncoder;


    @BeforeEach
    public void before(){

    }

    @Test
    public void 도서대여() throws Exception {
        //Given
        User user = User.builder()
                .name("도서대여테스트")
                .personalId("renttest")
                .password(passwordEncoder.encode("test"))
                .email("test@rent.com")
                .partType(PartType.MOBIS)
                .nickname("renttestnickname")
                .build();

        Book book = Book.builder()
                .title("테스트 제목")
                .author("테스트 작가")
                .publisher("테스트 출판")
                .isbn("123")
                .categoryType(CategoryType.NOVEL)
                .description("테스트 설명")
                .build();

        userRepository.save(user);
        bookRepository.save(book);
        //when
        rentService.createRent(user.getPersonalId(), book.getId());

        //then
        assertEquals(RentType.CLOSE, book.getRentType(), "도서 대여시 도서 상태는 CLOSE");
        assertEquals(1, book.getRentCount(), "도서 대여시 rentCount + 1");
    }


    @Test
    public void 도서대여_대여중() throws Exception {
        // given
        User user = User.builder()
                .name("도서대여테스트")
                .personalId("renttest")
                .password(passwordEncoder.encode("test"))
                .email("test@rent.com")
                .partType(PartType.MOBIS)
                .nickname("renttestnickname")
                .build();

        User user2 = User.builder()
                .name("도서대여테스트2")
                .personalId("renttest2")
                .password(passwordEncoder.encode("test"))
                .email("test2@rent.com")
                .partType(PartType.MOBIS)
                .nickname("renttestnickname2")
                .build();

        Book book = Book.builder()
                .title("테스트 제목")
                .author("테스트 작가")
                .publisher("테스트 출판")
                .isbn("123")
                .categoryType(CategoryType.NOVEL)
                .description("테스트 설명")
                .build();

        userRepository.save(user);
        userRepository.save(user2);
        bookRepository.save(book);

        // when
        //then
        rentService.createRent(user.getPersonalId(), book.getId());

        assertThrows(LibraryException.RentStateException.class, () -> {
            rentService.createRent(user2.getPersonalId(), book.getId());
        });

    }

    @Test
    public void 대여도서_반납() throws Exception {
        //given
        User user = User.builder()
                .name("도서대여테스트")
                .personalId("renttest")
                .password(passwordEncoder.encode("test"))
                .email("test@rent.com")
                .partType(PartType.MOBIS)
                .nickname("renttestnickname")
                .build();

        Book book = Book.builder()
                .title("테스트 제목")
                .author("테스트 작가")
                .publisher("테스트 출판")
                .isbn("123")
                .categoryType(CategoryType.NOVEL)
                .description("테스트 설명")
                .build();

        userRepository.save(user);
        bookRepository.save(book);

        //when
        Long rentId = rentService.createRent(user.getPersonalId(), book.getId());
        rentService.returnBook(rentId);

        Optional<Rent> rent = rentRepository.findById(rentId);
        if (rent.isPresent()) {
            //then
            assertEquals(RentType.OPEN, book.getRentType(), "도서 반납시 대여 가능 상태");
            assertNotNull(rent.get().getReturnDate(), "도서 반납시 returnDate가 null이 아님");
        }

    }

}