package hyundaiautoever.library.model.entity;

import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.common.type.RentType;
import hyundaiautoever.library.model.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Table(name = "BOOK")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id; // 도서 ID

    @Column(name = "title", nullable = false)
    private String title; // 책 제목

    @Column(name = "isbn", nullable = false)
    private String isbn;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "rentCount", nullable = false)
    private Integer rentCount;
    @Enumerated(EnumType.STRING)
    @Column(name = "rent_type", nullable = false)
    private RentType rentType; // 대출 가능 상태 [OPEN, CLOSE]

    @Column(name = "description", length = 5000)
    private String description; // 설명

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false)
    private CategoryType categoryType; // 카테고리 타입 [개발, 소설, 인문, 과학, 경제]

    @Column(name = "love_count", nullable = false)
    private Integer loveCount; // 좋아요 개수

    @Builder
    public Book(String title, String author, String publisher, String isbn, CategoryType categoryType, String description) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.categoryType = categoryType;
        this.description = description;
        this.rentCount = 0;
        this.rentType = RentType.OPEN;
        this.loveCount = 0;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateAuthor(String author) {
        this.author = author;
    }

    public void updateIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void updatePublisher(String publisher) {
        this.publisher = publisher;
    }

    public void updateRentType(RentType rentType) {
        this.rentType = rentType;
    }

    public void updateCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }
    public void updateLoveCount(Integer loveCount) {
        this.loveCount = loveCount;
    }

    public void updateRentCount(Integer rentCount) {
        this.rentCount = rentCount;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

}
