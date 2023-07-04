package hyundaiautoever.library.model.entity;

import hyundaiautoever.library.model.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aspectj.apache.bcel.classfile.LocalVariable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "RENT")
@NoArgsConstructor
public class Rent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rent_id")
    private Long id; // 대출 ID

    @Column(name = "rent_date", nullable = false)
    private LocalDate rentDate; // 대여일

    @Column(name = "expected_return_date", nullable = false)
    private LocalDate expectedReturnDate; // 반납예정일

    @Column(name = "return_date")
    private LocalDateTime returnDate; // 반납일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User rentUser; // 예약자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book rentBook; // 예약책
}
