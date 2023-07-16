package hyundaiautoever.library.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.model.entity.User;
import lombok.Getter;
import lombok.Setter;

public class LoginResponse {

    @Getter
    @Setter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class LoginResponseDto {
        private final int errorcode;
        
        private final String message;
        public LoginResponseDto(ExceptionCode code) {
            this.errorcode = code.getCode();
            this.message = code.getMessage();
        }
    }
}
