package hyundaiautoever.library.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import hyundaiautoever.library.model.entity.Book;
import lombok.Getter;

public class LoveDto {
    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class GetLoveDto {
        private final Long bookId;
        private final String title;
        private final String author;
        private final String isbn;

        public GetLoveDto(Book book) {
            this.bookId = book.getId();
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.isbn = book.getIsbn();
        }

    }
}
