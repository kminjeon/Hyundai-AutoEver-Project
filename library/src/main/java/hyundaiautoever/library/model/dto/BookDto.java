package hyundaiautoever.library.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.common.type.RentType;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.dto.response.Response.Pagination;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.User;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

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
    public static class CategoryBookDto {
        private final Long bookId;
        private final String title;
        private final String author;
        private final String img;
        private final RentType rentType;
        private final Integer loveCount;

        @QueryProjection
        public CategoryBookDto(Book book) {
            this.bookId = book.getId();
            this.author = book.getAuthor();
            this.img = book.getImg();
            this.title = book.getTitle();
            this.rentType = book.getRentType();
            this.loveCount = book.getLoveCount();
        }
    }

    @Getter
    public static class CategoryBookPage {
        private final Pagination pagination;
        private final List<CategoryBookDto> categoryBookDtoList;
        public CategoryBookPage(Page<CategoryBookDto> page) {
            this.pagination = new Pagination(page);
            this.categoryBookDtoList = page.getContent();
        }
    }


    public static BookDto.UpdateBookDto buildUpdateBookDto(Book book) {
        return new UpdateBookDto(book);
    }

    public static SearchAdminBookPage buildSearchAdminBookPage(Page<SearchAdminBookDto> page) {
        return new SearchAdminBookPage(page);
    }

    public static CategoryBookPage buildCategoryBookPage(Page<CategoryBookDto> page) {
        return new CategoryBookPage(page);
    }

}
