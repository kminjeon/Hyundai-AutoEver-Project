package hyundaiautoever.library.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.type.AuthType;
import hyundaiautoever.library.common.type.PartType;
import hyundaiautoever.library.model.entity.User;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class UserDto {

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class UserSimpleDto {
        private final String name;

        private final String email;
        public UserSimpleDto(User user) {
            this.name = user.getName();
            this.email = user.getEmail();
        }
    }

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class LoginDto {
        private final String name;

        private final String personalId;

        public LoginDto(User user) {
            this.name = user.getName();
            this.personalId = user.getPersonalId();
        }
    }

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class UserProfile { // 사용자 프로필 조회
        private final String name;
        private final String personalId;
        private final String email;
        private final AuthType authType;
        private final PartType part;
        private final String password;
        private final String nickname;

        private final String createDate;

        public UserProfile(User user) {
            this.name = user.getName();
            this.personalId = user.getPersonalId();
            this.email = user.getEmail();
            this.authType = user.getAuthType();
            this.part = user.getPart();
            this.password = user.getPassword();
            this.nickname = user.getNickname();
            this.createDate = user.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm"));
        }
    }

    public static UserProfile buildUserProfile(User user) {
        return new UserProfile(user);
    }

    public static UserSimpleDto buildUserSimpleDto(User user) {
        return new UserSimpleDto(user);
    }

    public static LoginDto buildLoginDto(User user) {
        return new LoginDto(user);
    }


    /*
    public static List<UserDto> buildUserList(List<User> userList) {
        return userList.stream().map(UserDto::new).collect(Collectors.toList());
    }
    */
}