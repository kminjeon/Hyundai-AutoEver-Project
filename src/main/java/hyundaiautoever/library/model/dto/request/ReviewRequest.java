package hyundaiautoever.library.model.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewRequest {

    @Getter
    @Setter
    public static class CreateReviewRequest {
        @NotEmpty(message = "내용은 필수입니다")
        @NotNull
        private String content;
        @NotEmpty(message = "사용자 아이디는 필수입니다")
        @NotNull
        private String personalId;
    }

    @Getter
    public static class UpdateReviewRequest {
        @NotEmpty(message = "내용은 필수입니다")
        @NotNull
        private String content;
    }
}
