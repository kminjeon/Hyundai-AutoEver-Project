package hyundaiautoever.library.model.entity;

import hyundaiautoever.library.common.type.RentType;
import hyundaiautoever.library.model.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "REVIEW")
@NoArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id; // 리뷰 ID

    @Column(name = "content", length = 500, nullable = false)
    private String content; // 리뷰 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User reviewUser; // 예약자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book reviewBook; // 예약책

    public void updateReviewContent(String content) {
        this.content = content;
    }

}
