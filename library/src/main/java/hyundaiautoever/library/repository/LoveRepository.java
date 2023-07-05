package hyundaiautoever.library.repository;

import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Love;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoveRepository extends JpaRepository<Love, Long> {
}
