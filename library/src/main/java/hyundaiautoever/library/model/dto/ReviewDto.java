package hyundaiautoever.library.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Review;
import hyundaiautoever.library.model.entity.User;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReviewDto {

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class CreateReviewDto {
        private final Long reviewId;
        private final String content;
        private final String nickname;
        private final String createDate;

        public CreateReviewDto(Review review) {
            this.content = review.getContent();
            this.reviewId = review.getId();
            this.nickname = review.getUser().getNickname();
            this.createDate = review.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        }
    }

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class GetReviewDetailDto {
        private final Long reviewId;
        private final String content;
        private final String nickname;
        private final String createDate;
        private final String title;
        private final String author;
        private final String publisher;

        public GetReviewDetailDto(Review review, User user, Book book) {
            this.content = review.getContent();
            this.reviewId = review.getId();
            this.nickname = user.getNickname();
            this.createDate = review.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            this.publisher = book.getPublisher();
            this.title = book.getTitle();
            this.author = book.getAuthor();
        }
    }

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class UpdateReviewDto {
        private final Long reviewId;
        private final String content;

        public UpdateReviewDto(Review review) {
            this.reviewId = review.getId();
            this.content = review.getContent();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class GetReviewDto {
        private final Long reviewId;
        private final String title;
        private final String author;
        private final String reviewDate;
        private final String isbn;
        private final Long bookId;
        private final String content;

        public GetReviewDto(Review review) {
            this.content = review.getContent();
            this.bookId = review.getBook().getId();
            this.isbn = review.getBook().getIsbn();
            this.reviewId = review.getId();
            this.title = review.getBook().getTitle();
            this.author = review.getBook().getAuthor();
            this.reviewDate = review.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        }
    }

    @Getter
    public static class GetReviewPage {
        private final Response.Pagination pagination;
        private final List<GetReviewDto> getReviewDtoList;
        public GetReviewPage(Page<GetReviewDto> page) {
            this.pagination = new Response.Pagination(page);
            this.getReviewDtoList = page.getContent();
        }
    }

    public static CreateReviewDto buildCreateReviewDto(Review review) {
        return new CreateReviewDto(review);
    }

    public static  GetReviewDetailDto buildGetReviewDetailDto(Review review, User user, Book book) {
        return new GetReviewDetailDto(review, user, book);
    }

    public static UpdateReviewDto buildUpdateReviewDto(Review review) {
        return new UpdateReviewDto(review);
    }

    public static GetReviewPage buildGetReviewPage(Page<GetReviewDto> page) {
        return new GetReviewPage(page);
    }
}
