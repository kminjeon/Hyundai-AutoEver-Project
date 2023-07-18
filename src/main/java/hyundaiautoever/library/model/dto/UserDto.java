package hyundaiautoever.library.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import hyundaiautoever.library.common.type.AuthType;
import hyundaiautoever.library.common.type.PartType;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.entity.User;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
        private final Long id;
        private final String personalId;
        private final String name;
        private final String email;
        private final AuthType authType;

        @QueryProjection
        public LoginDto(User user) {
            this.id = user.getId();
            this.personalId = user.getPersonalId();
            this.name = user.getName();
            this.email = user.getEmail();
            this.authType = user.getAuthType();
        }
    }

    @Getter
    public static class UserAuthPage {
        private final Response.Pagination pagination;
        private final List<LoginDto> userList;

        public UserAuthPage(Page<LoginDto> page) {
            this.pagination = new Response.Pagination(page);
            this.userList = page.getContent();
        }
    }

    @Getter
    public static class UserProfile { // 사용자 프로필 조회
        private final String name;
        private final String personalId;
        private final String email;
        private final AuthType authType;
        private final PartType partType;
        private final String nickname;
        private final LocalDateTime lastLogin;

        public UserProfile(User user) {
            this.name = user.getName();
            this.personalId = user.getPersonalId();
            this.email = user.getEmail();
            this.authType = user.getAuthType();
            this.partType = user.getPartType();
            this.nickname = user.getNickname();
            this.lastLogin = user.getLastLoginDate();
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

    public static UserAuthPage buildUserAuthPage(Page<UserDto.LoginDto> page) {
        return new UserAuthPage(page);
    }

    /*
    public static List<UserDto> buildUserList(List<User> userList) {
        return userList.stream().map(UserDto::new).collect(Collectors.toList());
    }
    */
}