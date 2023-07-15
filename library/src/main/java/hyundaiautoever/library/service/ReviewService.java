package hyundaiautoever.library.service;

import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.model.dto.ReviewDto;
import hyundaiautoever.library.model.dto.request.ReviewRequest;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Review;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.BookRepository;
import hyundaiautoever.library.repository.ReviewRepository;
import hyundaiautoever.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hyundaiautoever.library.model.dto.ReviewDto.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // 조회 성능 최적화
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    /**
     * 리뷰 생성
     * @param bookId
     * @param request
     * @return CreateReviewDto
     */
    @Transactional
    public Long createReview(Long bookId, ReviewRequest.CreateReviewRequest request) {
        Book book  = bookRepository.findById(bookId).orElseThrow(() -> {
            log.error("createReview Exception : [존재하지 않는 Book ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        User user = userRepository.findByPersonalId(request.getPersonalId()).orElseThrow(() -> {
            log.error("createReview Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });
        Review review = Review.builder()
                        .content(request.getContent())
                        .user(user)
                        .book(book)
                        .build();
        // Review 저장
        try {
            reviewRepository.save(review);
        } catch (Exception e) {
            log.error("createReview Exception : {}", e.getMessage());
            throw new LibraryException.DataSaveException(ExceptionCode.DATA_SAVE_EXCEPTION);
        }
        return review.getId();
    }


    /**
     * 리뷰 상세
     * @param reviewId
     * @return GetReviewDetailDto
     */
    public GetReviewDetailDto getReviewDetail(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
            log.error("getReviewDetail Exception : [존재하지 않는 Review ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        User user = userRepository.findById(review.getUser().getId()).orElseThrow(() -> {
            log.error("getReviewDetail Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        Book book = bookRepository.findById(review.getBook().getId()).orElseThrow(() -> {
            log.error("getReviewDetail Exception : [존재하지 않는 Book ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        return buildGetReviewDetailDto(review, user, book);
    }

    /**
     * 리뷰 페이지 조회
     * @param pageable
     * @param personalId
     * @return GetReviewPage
     */
    public GetReviewPage getReviewPage(Pageable pageable, String personalId) {
        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("getReviewPage Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });
        return ReviewDto.buildGetReviewPage(reviewRepository.findByUser(pageable, user));
    }

    /**
     * 리뷰 수정
     * @param request
     * @return
     */
    public UpdateReviewDto updateReview(ReviewRequest.UpdateReviewRequest request) {
        Review review = reviewRepository.findById(request.getReviewId()).orElseThrow(() -> {
            log.error("updateReview Exception : [존재하지 않는 Review ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        review.updateReviewContent(request.getContent()); // 프론트에서 수정되면 주는지 아니면 다주는지 보고 생각
        return buildUpdateReviewDto(review);
    }

    /**
     * 리뷰 삭제
     * @param reviewId
     */
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> {
            log.error("deleteReview Exception : [존재하지 않는 Review ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        // 리뷰 삭제
        try {
            reviewRepository.deleteById(reviewId);
        } catch (Exception e) {
            log.error("deleteReview Exception : {}", e.getMessage());
            throw new LibraryException.DataDeleteException(ExceptionCode.DATA_DELETE_EXCEPTION);
        }
    }
}
