package hyundaiautoever.library.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.model.entity.Book;
import lombok.Getter;

public class BookDto {

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class updateBookDto {
        private final String title;
        private final String author;
        private final String publisher;
        private final CategoryType category;
        private final String isbn;

        public updateBookDto (Book book) {
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.publisher = book.getPublisher();
            this.category = book.getCategoryType();
            this.isbn = book.getIsbn();
        }
    }

}
