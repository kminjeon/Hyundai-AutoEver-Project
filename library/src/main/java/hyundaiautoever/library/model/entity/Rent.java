package hyundaiautoever.library.model.entity;

import hyundaiautoever.library.model.entity.base.BaseEntity;
import lombok.*;
import org.aspectj.apache.bcel.classfile.LocalVariable;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "RENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(name = "extension_number")
    private Integer extensionNumber;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 예약자

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book; // 예약책

    @Builder
    public Rent(User user, Book book) {
        this.book = book;
        this.user = user;
        this.rentDate = LocalDate.now();
        this.expectedReturnDate = rentDate.plusDays(14);
        this.extensionNumber = 0;
    }

    public void updateExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public void updateExtensionNumber(Integer extensionNumber) {
        this.extensionNumber = extensionNumber;
    }

    public void updateReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }
}
