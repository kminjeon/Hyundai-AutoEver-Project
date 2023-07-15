package hyundaiautoever.library.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@Transactional
@SpringBootTest
class LoveServiceTest {

    @Autowired
    LoveService loveService;

    @Autowired
    UserService userService;

    @Autowired
    BookService bookService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    LoveRepository loveRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    public void 좋아요_생성() {
        // Given
        User user = createUser();
        Book book = createBook();
        userRepository.save(user);
        bookRepository.save(book);
        Integer count = book.getLoveCount();

        // When
        Long loveId = loveService.createLove(user.getPersonalId(), book.getId());
        Optional<Love> loveOptional = loveRepository.findById(loveId);

        // Then
        assertTrue(loveOptional.isPresent(), "좋아요가 정상적으로 생성되었음");
        Love love = loveOptional.get();
        assertEquals(book.getId(), love.getBook().getId(), "도서 ID 일치");
        assertEquals(user.getId(), love.getUser().getId(), "사용자 ID 일치");
        assertEquals(count + 1, bookRepository.findById(book.getId()).get().getLoveCount(), "도서 좋아요 수 증가");
    }


    private User createUser() {
        User user = new User("회원가입테스트","jointest", passwordEncoder.encode("test"),"test@join.com", PartType.MOBIS, "joinnickname");
        return user;
    }

    private Book createBook() {
        Book book = new Book("테스트제목", "테스트작가", "123", "테스트출판", CategoryType.NOVEL, "테스트설명");
        return book;
    }
}