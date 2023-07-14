package hyundaiautoever.library.model.entity;

import hyundaiautoever.library.common.type.RentType;
import hyundaiautoever.library.model.entity.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "REVIEW")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id; // 리뷰 ID

    @Column(name = "content", length = 500, nullable = false)
    private String content; // 리뷰 내용

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 리뷰 작성자

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book; // 리뷰 도서

    @Builder
    public Review(String content, User user, Book book) {
        this.content = content;
        this.user = user;
        this.book = book;
    }

    public void updateReviewContent(String content) {
        this.content = content;
    }

}
