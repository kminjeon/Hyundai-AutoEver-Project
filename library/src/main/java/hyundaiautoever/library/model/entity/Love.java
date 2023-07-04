package hyundaiautoever.library.model.entity;

import hyundaiautoever.library.model.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "LOVE")
@NoArgsConstructor
public class Love extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "love_id")
    private Long id; // 좋아요 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User LoveUser; // 예약자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book LoveBook; // 예약책

    @JoinColumn(name = "count", nullable = false)
    private Integer count; // 좋아요 개수
}
