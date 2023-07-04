package hyundaiautoever.library.model.entity;

import hyundaiautoever.library.model.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "RESERVE")
@NoArgsConstructor
public class Reserve extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserve_id")
    private Long id; // 예약 ID

    @Column(name = "wait_number", nullable = false)
    private Integer waitNumber; // 대기 순번

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User reserveUser; // 예약자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book reserveBook; // 예약책
}
