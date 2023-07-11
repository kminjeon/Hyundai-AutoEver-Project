package hyundaiautoever.library.model.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplyRequest {

    @Getter
    public static class CreateApplyRequest {
        private String title;
        private String author;
        private String publisher;
        private String isbn;
        private String personalId;
    }

    @Getter
    public static class updateApplyRequest {
        private Long applyId;
        private String title;
        private String author;
        private String publisher;
        private String isbn;
        private String applyUser;
    }
}
