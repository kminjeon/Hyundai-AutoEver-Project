package hyundaiautoever.library.repository;

import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentRepository extends JpaRepository<Rent, Long> {
}
