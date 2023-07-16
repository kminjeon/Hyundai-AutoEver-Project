package hyundaiautoever.library.model.dto.request;

import hyundaiautoever.library.common.type.CategoryType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookRequest {

    @Getter
    @Setter
    public static class AddBookRequest {
        @NotEmpty(message = "책 이름은 필수입니다")
        @NotNull
        private String title;
        @NotEmpty(message = "저자는 필수입니다")
        @NotNull
        private String author;
        @NotEmpty(message = "출판사는 필수입니다")
        @NotNull
        private String publisher;
        @NotEmpty(message = "isbn은 필수입니다")
        @NotNull
        private String isbn;
        @NotNull
        private CategoryType categoryType;
        private String description;
    }

    @Getter
    @Setter
    public static class updateBookRequest {

        private String title;
        private String author;
        private String publisher;
        private CategoryType categoryType;
        private String isbn;
        private String description;
    }
}
