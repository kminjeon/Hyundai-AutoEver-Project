package hyundaiautoever.library.model.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReserveRequest {
    @Getter
    public static class CreateReserveRequest {
        @NotEmpty(message = "사용자 ID는 필수입니다")
        @NotNull
        private String personalId;
        @NotEmpty(message = "책 ID은 필수입니다")
        @NotNull
        private Long bookId;
    }



}
