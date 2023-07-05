package hyundaiautoever.library.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import hyundaiautoever.library.model.entity.Apply;
import lombok.Getter;

public class ApplyDto {

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class UpdateApplyDto {
        private final String title;
        private final String author;
        private final String publisher;
        private final String isbn;

        public UpdateApplyDto(Apply apply) {
            this.title = apply.getTitle();
            this.author = apply.getAuthor();
            this.publisher = apply.getPublisher();
            this.isbn = apply.getIsbn();
        }
    }

    public static UpdateApplyDto buildUpdateApplyDto(Apply apply) {
        return new UpdateApplyDto(apply);
    }
}
