package hyundaiautoever.library.model.entity;

import hyundaiautoever.library.model.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "LOVE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Love extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "love_id")
    private Long id; // 좋아요 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 좋아요 누른 예약자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book; // 좋아요 누른 책

    @Builder
    public Love(User user, Book book) {
        this.user = user;
        this.book = book;
    }
}
