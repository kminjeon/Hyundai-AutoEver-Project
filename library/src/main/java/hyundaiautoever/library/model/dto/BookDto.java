package hyundaiautoever.library.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.common.type.RentType;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.dto.response.Response.Pagination;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Review;
import hyundaiautoever.library.model.entity.User;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static hyundaiautoever.library.model.dto.ReviewDto.*;

public class BookDto {

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class UpdateBookDto {
        private final String title;
        private final String author;
        private final String publisher;
        private final CategoryType categoryType;
        private final String isbn;

        public UpdateBookDto (Book book) {
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.publisher = book.getPublisher();
            this.categoryType = book.getCategoryType();
            this.isbn = book.getIsbn();
        }
    }

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class SearchAdminBookDto {
        private final Long bookId;
        private final CategoryType categoryType;
        private final String title;
        private final RentType rentType;

        @QueryProjection
        public SearchAdminBookDto(Book book) {
            this.bookId = book.getId();
            this.categoryType = book.getCategoryType();
            this.title = book.getTitle();
            this.rentType = book.getRentType();
        }
    }

    @Getter
    public static class SearchAdminBookPage {
        private final Pagination pagination;
        private final List<SearchAdminBookDto> adminBookDtoList;
        public SearchAdminBookPage(Page<SearchAdminBookDto> page) {
                this.pagination = new Pagination(page);
                this.adminBookDtoList = page.getContent();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class SimpleBookDto {
        private final Long bookId;
        private final String title;
        private final String author;
        private final String img;
        private final RentType rentType;
        private final Integer loveCount;

        @QueryProjection
        public SimpleBookDto(Book book) {
            this.bookId = book.getId();
            this.author = book.getAuthor();
            this.img = book.getImg();
            this.title = book.getTitle();
            this.rentType = book.getRentType();
            this.loveCount = book.getLoveCount();
        }
    }

    @Getter
    public static class SimpleBookPage {
        private final Pagination pagination;
        private final List<SimpleBookDto> simpleBookDtoList;
        public SimpleBookPage(Page<SimpleBookDto> page) {
            this.pagination = new Pagination(page);
            this.simpleBookDtoList = page.getContent();
        }
    }

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class GetBookDetailDto {
        private final Long bookId;
        private final String title;
        private final String author;
        private final String publisher;
        private final CategoryType categoryType;
        private final String isbn;
        private final RentType rentType;
        private final String description;
        private final String img;
        private final Integer loveCount;
        private final List<CreateReviewDto> reviewList;
        public GetBookDetailDto(Book book, List<CreateReviewDto> reviewList) {
            this.bookId = book.getId();
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.publisher = book.getPublisher();
            this.categoryType = book.getCategoryType();
            this.isbn = book.getIsbn();
            this.rentType = book.getRentType();
            this.description = book.getDescription();
            this.img = book.getImg();
            this.loveCount = book.getLoveCount();
            this.reviewList = reviewList;
        }
    }


    public static BookDto.UpdateBookDto buildUpdateBookDto(Book book) {
        return new UpdateBookDto(book);
    }

    public static SearchAdminBookPage buildSearchAdminBookPage(Page<SearchAdminBookDto> page) {
        return new SearchAdminBookPage(page);
    }

    public static SimpleBookPage buildCategoryBookPage(Page<SimpleBookDto> page) {
        return new SimpleBookPage(page);
    }

    public static GetBookDetailDto buildGetBookDetailDto(Book book, List<CreateReviewDto> reviewList) {
        return new GetBookDetailDto(book, reviewList);
    }

}
