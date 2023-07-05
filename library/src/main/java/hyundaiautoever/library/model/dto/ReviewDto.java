package hyundaiautoever.library.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Review;
import hyundaiautoever.library.model.entity.User;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

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
            this.nickname = review.getReviewUser().getNickname();
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
        private final String img;

        public GetReviewDetailDto(Review review, User user, Book book) {
            this.content = review.getContent();
            this.reviewId = review.getId();
            this.nickname = user.getNickname();
            this.createDate = review.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            this.publisher = book.getPublisher();
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.img = book.getImg();
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


    public static ReviewDto.CreateReviewDto buildCreateReviewDto(Review review) {
        return new CreateReviewDto(review);
    }

    public static  ReviewDto.GetReviewDetailDto buildGetReviewDetailDto(Review review, User user, Book book) {
        return new GetReviewDetailDto(review, user, book);
    }

    public static ReviewDto.UpdateReviewDto buildUpdateReviewDto(Review review) {
        return new UpdateReviewDto(review);
    }
}
