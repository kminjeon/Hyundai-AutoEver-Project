package hyundaiautoever.library.model.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplyRequest {

    @Getter
    @Setter
    public static class CreateApplyRequest {
        private String title;
        private String author;
        private String publisher;
        private String isbn;
        private String personalId;
    }

    @Getter
    @Setter
    public static class UpdateApplyRequest {
        private String title;
        private String author;
        private String publisher;
        private String isbn;
    }
}
