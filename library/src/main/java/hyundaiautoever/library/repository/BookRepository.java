package hyundaiautoever.library.repository;

import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.model.dto.BookDto;
import hyundaiautoever.library.model.dto.BookDto.CategoryBookDto;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface BookRepository extends JpaRepository<Book, Long>, BookRepositorySupport {

    @Query("SELECT new hyundaiautoever.library.model.dto.BookDto$CategoryBookDto(b) FROM Book b WHERE b.categoryType = :categoryType")
    Page<CategoryBookDto> findByCategoryType(Pageable pageable, @Param("categoryType") CategoryType categoryType);
}
