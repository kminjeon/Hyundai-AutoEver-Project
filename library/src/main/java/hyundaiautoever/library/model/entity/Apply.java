package hyundaiautoever.library.model.entity;

import hyundaiautoever.library.model.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
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
    @JoinColumn(name = "user_id")
    private User applyUser;

}
