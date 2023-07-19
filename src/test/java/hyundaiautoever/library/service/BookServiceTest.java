package hyundaiautoever.library.service;

import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.common.type.PartType;
import hyundaiautoever.library.common.type.RentType;
import hyundaiautoever.library.model.dto.request.BookRequest;
import hyundaiautoever.library.model.dto.request.UserRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.*;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LoveRepository loveRepository;

    @Mock
    private RentRepository rentRepository;

    @Mock
    private ReserveRepository reserveRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    public void 도서_추가() throws Exception {
        //given
        BookRequest.AddBookRequest request = createBookRequest();
        Book book = createBookEntity(request);

        //when
        given(bookRepository.save(any())).willReturn(book);
        given(bookRepository.findById(any()))
                .willReturn(Optional.ofNullable(book));

        //when
        Long bookId = bookService.addBook(request);

        //then
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        assertTrue(optionalBook.isPresent());
        Book savedBook = optionalBook.get();
        assertEquals(request.getTitle(), savedBook.getTitle());
    }

    @Test
    public void 도서_수정() throws Exception {
        //given
        BookRequest.updateBookRequest request = updateBookRequest();

        Book existingBook = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .publisher(request.getPublisher())
                .isbn(request.getIsbn())
                .categoryType(request.getCategoryType())
                .description(request.getDescription())
                .build();
        ReflectionTestUtils.setField(existingBook, "id", 1L);

        given(bookRepository.findById(existingBook.getId())).willReturn(Optional.of(existingBook));

        //when
        bookService.updateBook(existingBook.getId(), request);

        //then
        Book book = bookRepository.findById(existingBook.getId()).orElseThrow(() -> new AssertionError("도서를 찾을 수 없음"));
        assertAll("도서 수정 검증",
                () -> assertEquals(request.getTitle(), book.getTitle(), "도서 수정 제목 동일"),
                () -> assertEquals(request.getAuthor(), book.getAuthor(), "도서 수정 작가 동일"),
                () -> assertEquals(request.getIsbn(), book.getIsbn(), "도서 수정 ISBN 동일"),
                () -> assertEquals(request.getPublisher(), book.getPublisher(), "도서 수정 출판사 동일"),
                () -> assertEquals(request.getCategoryType(), book.getCategoryType(), "도서 수정 카테고리 동일"),
                () -> assertEquals(request.getDescription(), book.getDescription(), "도서 수정 설명 동일")
        );    }

    @Test
    public void 도서_삭제() throws Exception {
        //given
        BookRequest.updateBookRequest request = updateBookRequest();
        Book existingBook = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .publisher(request.getPublisher())
                .isbn(request.getIsbn())
                .categoryType(request.getCategoryType())
                .description(request.getDescription())
                .build();
        ReflectionTestUtils.setField(existingBook, "id", 1L);

        given(bookRepository.findById(existingBook.getId())).willReturn(Optional.of(existingBook));

        //when
        Response response = bookService.deleteBook(existingBook.getId());

        //then
        assertEquals(Response.ok().getCode(), response.getCode());
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

    private Book createBookEntity(BookRequest.AddBookRequest request) {
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .publisher(request.getPublisher())
                .isbn(request.getIsbn())
                .categoryType(request.getCategoryType())
                .description(request.getDescription())
                .build();

        ReflectionTestUtils.setField(book, "id", 1L);
        return book;
    }
}