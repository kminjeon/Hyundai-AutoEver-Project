package hyundaiautoever.library.service;

import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.common.type.PartType;
import hyundaiautoever.library.model.dto.ReviewDto;
import hyundaiautoever.library.model.dto.request.ApplyRequest;
import hyundaiautoever.library.model.dto.request.BookRequest;
import hyundaiautoever.library.model.dto.request.ReviewRequest;
import hyundaiautoever.library.model.dto.request.UserRequest;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Review;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.BookRepository;
import hyundaiautoever.library.repository.ReviewRepository;
import hyundaiautoever.library.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ReviewServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookService bookService;
    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void 리뷰_등록() throws Exception {
        //Given
        UserRequest.CreateUserRequest userRequest = createUserRequest();
        userService.createUser(userRequest);
        BookRequest.AddBookRequest bookRequest = createBookRequest();
        Long bookId = bookService.addBook(bookRequest);
        ReviewRequest.CreateReviewRequest reviewRequest = createReviewRequest(userRequest.getPersonalId());

        //when
        Long reviewId = reviewService.createReview(bookId, reviewRequest);
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);

        //then
        assertTrue(reviewOptional.isPresent());
        assertEquals(userRequest.getPersonalId(), reviewOptional.get().getUser().getPersonalId());
        assertEquals(bookId, reviewOptional.get().getBook().getId());
    }

    @Test
    public void 리뷰_수정() throws Exception {
        //given
        UserRequest.CreateUserRequest userRequest = createUserRequest();
        User user = createUserEntity(userRequest);
        userRepository.save(user);
        BookRequest.AddBookRequest bookRequest = createBookRequest();
        Book book = createBookEntity(bookRequest);
        bookRepository.save(book);
        ReviewRequest.CreateReviewRequest reviewRequest = createReviewRequest(userRequest.getPersonalId());
        Review review = createReviewEntity(reviewRequest,user, book);
        reviewRepository.save(review);

        ReviewRequest.UpdateReviewRequest request = new ReviewRequest.UpdateReviewRequest();
        request.setContent("업데이트");

        //when
        ReviewDto.UpdateReviewDto dto = reviewService.updateReview(review.getId(), request);

        //then
        assertEquals(request.getContent(), dto.getContent());
    }

    @Test
    public void 리뷰_삭제() throws Exception {
        UserRequest.CreateUserRequest userRequest = createUserRequest();
        User user = createUserEntity(userRequest);
        userRepository.save(user);
        BookRequest.AddBookRequest bookRequest = createBookRequest();
        Book book = createBookEntity(bookRequest);
        bookRepository.save(book);
        ReviewRequest.CreateReviewRequest reviewRequest = createReviewRequest(userRequest.getPersonalId());
        Review review = createReviewEntity(reviewRequest,user, book);
        reviewRepository.save(review);

        reviewService.deleteReview(review.getId());

        //then
        assertEquals(Optional.empty(), reviewRepository.findById(review.getId()));

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

    private ReviewRequest.CreateReviewRequest createReviewRequest(String personalId) {
        ReviewRequest.CreateReviewRequest request = new ReviewRequest.CreateReviewRequest();
        request.setContent("content test");
        request.setPersonalId(personalId);
        return request;
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

    private User createUserEntity(UserRequest.CreateUserRequest request) {
        User user = User.builder()
                .name(request.getName())
                .personalId(request.getPersonalId())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .partType(request.getPartType())
                .nickname(request.getNickname())
                .build();

        return user;
    }

    private Review createReviewEntity(ReviewRequest.CreateReviewRequest request, User user, Book book) {
        Review review = Review.builder()
                .content(request.getContent())
                .user(user)
                .book(book)
                .build();
        return review;
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
        return book;
    }
}