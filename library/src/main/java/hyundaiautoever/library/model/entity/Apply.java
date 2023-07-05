package hyundaiautoever.library.model.entity;

import hyundaiautoever.library.model.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "APPLY")
@NoArgsConstructor
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
    @JoinColumn(name = "apply_user")
    private User applyUser;

    @Builder
    public Apply(String title, String isbn, String author, String publisher, User applyUser) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.publisher = publisher;
        this.applyUser = applyUser;
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
