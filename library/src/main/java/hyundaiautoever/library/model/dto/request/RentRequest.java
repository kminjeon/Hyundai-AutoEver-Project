package hyundaiautoever.library.model.dto.request;

import hyundaiautoever.library.common.type.PartType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RentRequest {
    @Getter
    public static class CreateRentRequest {
        private String personalId;
        private Long bookId;
    }
}
