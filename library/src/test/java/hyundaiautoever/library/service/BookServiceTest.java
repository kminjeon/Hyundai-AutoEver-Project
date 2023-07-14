package hyundaiautoever.library.service;

import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.common.type.PartType;
import hyundaiautoever.library.common.type.RentType;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.BookRepository;
import hyundaiautoever.library.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class BookServiceTest {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RentService rentService;
    @Autowired
    PasswordEncoder passwordEncoder;

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
}