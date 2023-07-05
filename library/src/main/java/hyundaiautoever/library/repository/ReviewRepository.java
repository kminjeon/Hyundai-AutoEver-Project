package hyundaiautoever.library.repository;

import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
