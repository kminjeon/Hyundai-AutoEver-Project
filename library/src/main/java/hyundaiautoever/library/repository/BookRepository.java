package hyundaiautoever.library.repository;

import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.model.dto.BookDto.SimpleBookDto;
import hyundaiautoever.library.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, BookRepositorySupport {

    @Query("SELECT new hyundaiautoever.library.model.dto.BookDto$SimpleBookDto(b) FROM Book b WHERE b.categoryType = :categoryType")
    Page<SimpleBookDto> findPageByCategoryType(Pageable pageable, @Param("categoryType") CategoryType categoryType);

    Page<Book> findByCategoryType(Pageable pageable, CategoryType categoryType);
    List<Book> findTop10ByOrderByRentCountDesc();
}
