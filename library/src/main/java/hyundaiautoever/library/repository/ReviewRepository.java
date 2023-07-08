package hyundaiautoever.library.repository;

import hyundaiautoever.library.model.dto.ReviewDto;
import hyundaiautoever.library.model.dto.ReviewDto.GetReviewDto;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Review;
import hyundaiautoever.library.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByBook(Book book);

    @Query("SELECT new hyundaiautoever.library.model.dto.ReviewDto$GetReviewDto(r) FROM Review r WHERE r.user = :user")
    Page<GetReviewDto> findByUser(Pageable pageable, @Param("user") User user);
}
