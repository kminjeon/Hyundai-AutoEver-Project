package hyundaiautoever.library.model.dto.request;

import hyundaiautoever.library.common.type.PartType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRequest {

    @Getter
    public static class LoginRequest {
        @NotEmpty(message = "회원 아이디은 필수입니다")
        @NotNull
        private String personalId;
        @NotEmpty(message = "회원 비밀번호는 필수입니다")
        @NotNull
        private String password;
    }

    @Getter
    public static class UpdateProfileRequest {
        @NotEmpty(message = "회원 아이디은 필수입니다")
        @NotNull
        private String personalId;
        @NotEmpty(message = "회원 비밀번호는 필수입니다")
        @NotNull
        private String password;
        private String newPassword;
        private String email;
        private String nickname;
    }
    @Getter
    public static class CreateUserRequest {
        @NotEmpty(message = "회원 이름은 필수입니다")
        @NotNull
        private String name;
        @NotEmpty(message = "회원 아이디은 필수입니다")
        @NotNull
        private String personalId;
        @NotEmpty(message = "회원 비밀번호는 필수입니다")
        @NotNull
        private String password;
        @NotEmpty(message = "회원 이메일은 필수입니다")
        @NotNull
        private String email;
        @NotNull
        private PartType partType;
        @NotEmpty(message = "회원 닉네임은 필수입니다")
        @NotNull
        private String nickname;
    }
}
