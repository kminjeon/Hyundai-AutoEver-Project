package hyundaiautoever.library.model.entity;

import hyundaiautoever.library.common.type.AuthType;
import hyundaiautoever.library.common.type.PartType;
import hyundaiautoever.library.model.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "USER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name", nullable = false, length=10)
    private String name;

    @Column(name = "personal_id", nullable = false, length=20, unique = true)
    private String personalId;

    @Column(name = "email", nullable = false, length=50, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "part_type", nullable = false)
    @Enumerated(EnumType.STRING) // String으로 DB 저장
    private PartType partType;

    @Column(name = "nickname", nullable = false, length=50, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING) // String으로 DB 저장
    @Column(name = "auth_type")
    private AuthType authType;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Love> loves; // 좋아요 목록

    @Builder
    public User(String name, String personalId, String password, String email, PartType partType, String nickname) {
        this.name = name;
        this.personalId = personalId;
        this.password = password;
        this.email = email;
        this.partType = partType;
        this.nickname = nickname;
        this.authType = AuthType.USER;
        this.lastLoginDate = null;
    }

    public void updateUserEmail(String email) {
        this.email = email;
    }
    public void updateUserPassword(String password) {
        this.password = password;
    }
    public void updateUserPart(PartType partType) {
        this.partType = partType;
    }
    public void updateUserAuthType(AuthType authType) {
        this.authType = authType;
    }
    public void updateLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public void updateUserNickname(String nickname) {
        this.nickname = nickname;
    }
}

