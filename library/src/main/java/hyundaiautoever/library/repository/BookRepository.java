package hyundaiautoever.library.repository;

import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


}
