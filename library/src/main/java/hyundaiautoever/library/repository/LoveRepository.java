package hyundaiautoever.library.repository;

import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Love;
import hyundaiautoever.library.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoveRepository extends JpaRepository<Love, Long> {


    List<Love> findByUser(User user);
}
