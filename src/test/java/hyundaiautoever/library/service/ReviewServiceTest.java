package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @InjectMocks
    ReviewService reviewService;

    @Mock
    UserRepository userRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    public void 리뷰_등록_성공() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        ReviewRequest.CreateReviewRequest request = createReviewRequest(user.getPersonalId());

        //mock
        given(userRepository.findByPersonalId(user.getPersonalId())).willReturn(Optional.ofNullable(user));
        given(bookRepository.findById(book.getId())).willReturn(Optional.ofNullable(book));


        //when && then
        assertDoesNotThrow(() -> reviewService.createReview(book.getId(), request));
    }

    @Test
    public void 리뷰_등록_NOTFOUNT_book_예외() throws Exception {
        //given
        User user = createUser();
        Long bookId = 1L;
        ReviewRequest.CreateReviewRequest request = createReviewRequest(user.getPersonalId());

        //mock
        given(bookRepository.findById(bookId)).willReturn(Optional.empty());

        //when
        LibraryException.DataNotFoundException exception = assertThrows(
                LibraryException.DataNotFoundException.class,
                () -> reviewService.createReview(bookId, request)
        );

        //then
        assertEquals(ExceptionCode.DATA_NOT_FOUND_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
    }

    @Test
    public void 리뷰_등록_NOTFOUNT_user_예외() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        ReviewRequest.CreateReviewRequest request = createReviewRequest(user.getPersonalId());

        //mock
        given(bookRepository.findById(book.getId())).willReturn(Optional.ofNullable(book));
        given(userRepository.findByPersonalId(user.getPersonalId())).willReturn(Optional.empty());

        //when
        LibraryException.DataNotFoundException exception = assertThrows(
                LibraryException.DataNotFoundException.class,
                () -> reviewService.createReview(book.getId(), request)
        );

        //then
        assertEquals(ExceptionCode.DATA_NOT_FOUND_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
    }

    @Test
    public void 리뷰_등록_SAVE_예외() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        ReviewRequest.CreateReviewRequest request = createReviewRequest(user.getPersonalId());

        //mock
        given(userRepository.findByPersonalId(user.getPersonalId())).willReturn(Optional.ofNullable(user));
        given(bookRepository.findById(book.getId())).willReturn(Optional.ofNullable(book));
        doThrow(new RuntimeException("error")).when(reviewRepository).save(any());

        //when
        LibraryException.DataSaveException exception = assertThrows(
                LibraryException.DataSaveException.class,
                () -> reviewService.createReview(book.getId(), request)
        );

        //then
        assertEquals(ExceptionCode.DATA_SAVE_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
        verify(userRepository, times(1)).findByPersonalId(user.getPersonalId());
        verify(bookRepository, times(1)).findById(book.getId());
        verify(reviewRepository, times(1)).save(any());
    }


    @Test
    public void 리뷰_수정_성공() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        Review review = createReview(user, book);

        ReviewRequest.UpdateReviewRequest request = new ReviewRequest.UpdateReviewRequest();
        request.setContent("업데이트");

        //mock
        given(reviewRepository.findById(review.getId())).willReturn(Optional.ofNullable(review));

        //when
        assertDoesNotThrow(() -> reviewService.updateReview(review.getId(), request));

        //then
        assertEquals(request.getContent(), review.getContent());
    }

    @Test
    public void 리뷰_수정_NOTFOUND_예외() throws Exception {
        //given
        Long reviewId = 1L;

        ReviewRequest.UpdateReviewRequest request = new ReviewRequest.UpdateReviewRequest();
        request.setContent("업데이트");

        //mock
        given(reviewRepository.findById(reviewId)).willReturn(Optional.empty());

        //when
        LibraryException.DataNotFoundException exception = assertThrows(
                LibraryException.DataNotFoundException.class,
                () -> reviewService.updateReview(reviewId, request)
        );

        //then
        assertEquals(ExceptionCode.DATA_NOT_FOUND_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
    }

    @Test
    public void 리뷰_삭제_성공() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        Review review = createReview(user, book);

        //mock
        given(reviewRepository.findById(review.getId())).willReturn(Optional.ofNullable(review));

        //when && then
        assertDoesNotThrow(() -> reviewService.deleteReview(review.getId()));
    }

    @Test
    public void 리뷰_삭제_NOTFOUND_예외() throws Exception {
        //given
        Long reviewId = 1L;

        //mock
        given(reviewRepository.findById(reviewId)).willReturn(Optional.empty());

        //when
        LibraryException.DataNotFoundException exception = assertThrows(
                LibraryException.DataNotFoundException.class,
                () -> reviewService.deleteReview(reviewId)
        );

        //then
        assertEquals(ExceptionCode.DATA_NOT_FOUND_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
    }

    @Test
    public void 리뷰_삭제_DELETE_예외() throws Exception {
        //given
        User user = createUser();
        Book book = createBook();
        Review review = createReview(user, book);

        //mock
        given(reviewRepository.findById(review.getId())).willReturn(Optional.ofNullable(review));
        doThrow(new RuntimeException("error")).when(reviewRepository).deleteById(review.getId());

        //when
        LibraryException.DataDeleteException exception = assertThrows(
                LibraryException.DataDeleteException.class,
                () -> reviewService.deleteReview(review.getId())
        );

        //then
        assertEquals(ExceptionCode.DATA_DELETE_EXCEPTION.getCode(), exception.getExceptionCode().getCode());
    }

    private ReviewRequest.CreateReviewRequest createReviewRequest(String personalId) {
        ReviewRequest.CreateReviewRequest request = new ReviewRequest.CreateReviewRequest();
        request.setContent("content test");
        request.setPersonalId(personalId);
        return request;
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

    private Review createReview(User user, Book book) {
        Review review = new Review("리뷰내용", user, book);
        ReflectionTestUtils.setField(review, "id", 1L);
        return review;
    }
}