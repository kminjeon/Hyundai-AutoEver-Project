package hyundaiautoever.library.model.entity;

import hyundaiautoever.library.model.entity.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "APPLY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Apply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id")
    private Long id; // 신청 ID

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "isbn", nullable = false)
    private String isbn;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Apply(String title, String isbn, String author, String publisher, User user) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.publisher = publisher;
        this.user = user;
    }

    public void updateApplyTitle(String title) {
        this.title = title;
    }
    public void updateApplyAuthor(String author) {
        this.author = author;
    }
    public void updateApplyPublisher(String publisher) {
        this.publisher = publisher;
    }
    public void updateApplyIsbn(String isbn) {
        this.isbn = isbn;
    }
}
