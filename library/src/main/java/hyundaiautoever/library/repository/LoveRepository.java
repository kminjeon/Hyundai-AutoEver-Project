package hyundaiautoever.library.repository;

import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Love;
import hyundaiautoever.library.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoveRepository extends JpaRepository<Love, Long> {

    List<Love> findByUser(User user);

    Optional<Love> findByUserAndBook(User user, Book book);

    List<Love> findByUserAndBookIn(User user, List<Book> books);

    void deleteAllByUser(User user);
    void deleteAllByBook(Book book);

}
