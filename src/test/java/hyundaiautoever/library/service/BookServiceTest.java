package hyundaiautoever.library.service;

import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.common.type.RentType;
import hyundaiautoever.library.model.dto.request.BookRequest;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.repository.BookRepository;
import hyundaiautoever.library.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class BookServiceTest {

    @Autowired
    BookService bookService;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void 도서_추가() throws Exception {
        //given
        BookRequest.AddBookRequest request = createBookRequest();

        //when
        Long bookId = bookService.addBook(request);

        Optional<Book> book = bookRepository.findById(bookId);

        //then
        assertTrue(book.isPresent(), "도서가 정상적으로 추가되었음");
        assertEquals(request.getTitle(), book.get().getTitle(), "도서 추가 제목 동일");
        assertEquals(RentType.OPEN, book.get().getRentType(), "도서 추가시 도서 상태는 OPEN");

    }

    @Test
    public void 도서_수정() throws Exception {
        //given
        BookRequest.AddBookRequest CreateRequest = createBookRequest();

        Long bookId = bookService.addBook(CreateRequest);

        //when
        BookRequest.updateBookRequest updateRequest = updateBookRequest();

        bookService.updateBook(bookId, updateRequest);

        //then
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new AssertionError("도서를 찾을 수 없음"));
        assertAll("도서 수정 검증",
                () -> assertEquals(updateRequest.getTitle(), book.getTitle(), "도서 수정 제목 동일"),
                () -> assertEquals(updateRequest.getAuthor(), book.getAuthor(), "도서 수정 작가 동일"),
                () -> assertEquals(updateRequest.getIsbn(), book.getIsbn(), "도서 수정 ISBN 동일"),
                () -> assertEquals(updateRequest.getPublisher(), book.getPublisher(), "도서 수정 출판사 동일"),
                () -> assertEquals(updateRequest.getCategoryType(), book.getCategoryType(), "도서 수정 카테고리 동일"),
                () -> assertEquals(updateRequest.getDescription(), book.getDescription(), "도서 수정 설명 동일")
        );    }

    @Test
    public void 도서_삭제() throws Exception {

    }

    private BookRequest.AddBookRequest createBookRequest() {
        BookRequest.AddBookRequest request = new BookRequest.AddBookRequest();
        request.setTitle("테스트 제목");
        request.setAuthor("테스트 작가");
        request.setIsbn("123");
        request.setPublisher("테스트 출판");
        request.setCategoryType(CategoryType.NOVEL);
        request.setDescription("테스트 설명");
        return request;
    }

    private BookRequest.updateBookRequest updateBookRequest() {
        BookRequest.updateBookRequest request = new BookRequest.updateBookRequest();
        request.setTitle("테스트 제목2");
        request.setAuthor("테스트 작가2");
        request.setIsbn("1233");
        request.setPublisher("테스트 출판2");
        request.setCategoryType(CategoryType.DEV);
        request.setDescription("테스트 설명2");
        return request;
    }
}