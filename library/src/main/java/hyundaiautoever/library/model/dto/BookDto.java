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

        private final String description;

        public UpdateBookDto (Book book) {
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.publisher = book.getPublisher();
            this.categoryType = book.getCategoryType();
            this.isbn = book.getIsbn();
            this.description = book.getDescription();
        }
    }

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class SearchAdminBookDto {
        private final Long bookId;
        private final CategoryType categoryType;
        private final String title;
        private final String author;
        private final String isbn;
        private final Boolean rentType;

        @QueryProjection
        public SearchAdminBookDto(Book book) {
            this.bookId = book.getId();
            this.categoryType = book.getCategoryType();
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.isbn = book.getIsbn();
            this.rentType = book.getRentType().equals(RentType.OPEN) ? true : false;
        }
    }

    @Getter
    public static class SearchAdminBookPage {
        private final Pagination pagination;
        private final List<SearchAdminBookDto> bookList;
        public SearchAdminBookPage(Page<SearchAdminBookDto> page) {
                this.pagination = new Pagination(page);
                this.bookList = page.getContent();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class SimpleBookDto {
        private final Long bookId;
        private final String title;
        private final String author;
        private final String isbn;
        private final Boolean rentType;
        private final Integer loveCount;

        @QueryProjection
        public SimpleBookDto(Book book) {
            this.bookId = book.getId();
            this.author = book.getAuthor();
            this.isbn = book.getIsbn();
            this.title = book.getTitle();
            this.rentType = book.getRentType().equals(RentType.OPEN) ? true : false;
            this.loveCount = book.getLoveCount();
        }
    }

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class BestBookDto {
        private final Long bookId;
        private final String title;
        private final String author;
        private final String isbn;
        private final Boolean rentType;
        private final Integer loveCount;

        private final Integer rentCount;

        private final Boolean heart;

        public BestBookDto(Book book, Boolean heart) {
            this.bookId = book.getId();
            this.author = book.getAuthor();
            this.isbn = book.getIsbn();
            this.title = book.getTitle();
            this.rentType = book.getRentType().equals(RentType.OPEN) ? true : false;
            this.loveCount = book.getLoveCount();
            this.rentCount = book.getRentCount();
            this.heart = heart;
        }
    }

    @Getter
    public static class SimpleBookPage {
        private final Pagination pagination;
        private final List<SimpleBookDto> bookList;
        public SimpleBookPage(Page<SimpleBookDto> page) {
            this.pagination = new Pagination(page);
            this.bookList = page.getContent();
        }
    }

    @Getter
    public static class HeartAddBookPage {
        private final Pagination pagination;
        private final List<BestBookDto> bookList;
        public HeartAddBookPage(Page<Book> page, List<BestBookDto> bookList) {
            this.pagination = new Pagination(page);
            this.bookList = bookList;
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
        private final Boolean rentType;
        private final String description;
        private final Integer loveCount;
        private final Boolean heart;
        private final List<CreateReviewDto> reviewList;
        public GetBookDetailDto(Book book, List<CreateReviewDto> reviewList, Boolean heart) {
            this.bookId = book.getId();
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.publisher = book.getPublisher();
            this.categoryType = book.getCategoryType();
            this.isbn = book.getIsbn();
            this.rentType = book.getRentType().equals(RentType.OPEN) ? true : false;
            this.description = book.getDescription();
            this.loveCount = book.getLoveCount();
            this.heart = heart;
            this.reviewList = reviewList;
        }
    }

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class GetAdminBookDetailDto {
        private final Long bookId;
        private final String title;
        private final String author;
        private final String publisher;
        private final CategoryType categoryType;
        private final String isbn;
        private final Boolean rentType;
        private final String description;
        private final Integer loveCount;
        private final List<CreateReviewDto> reviewList;
        public GetAdminBookDetailDto(Book book, List<CreateReviewDto> reviewList) {
            this.bookId = book.getId();
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.publisher = book.getPublisher();
            this.categoryType = book.getCategoryType();
            this.isbn = book.getIsbn();
            this.rentType = book.getRentType().equals(RentType.OPEN) ? true : false;
            this.description = book.getDescription();
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

    public static GetBookDetailDto buildGetBookDetailDto(Book book, List<CreateReviewDto> reviewList, Boolean heart) {
        return new GetBookDetailDto(book, reviewList, heart);
    }

    public static HeartAddBookPage buildHeartAddBookPage(Page<Book> page, List<BestBookDto> bookList) {
        return new HeartAddBookPage(page, bookList);
    }

    public static GetAdminBookDetailDto buildAdminGetBookDetailDto(Book book, List<CreateReviewDto> reviewList) {
        return new GetAdminBookDetailDto(book, reviewList);
    }

}
