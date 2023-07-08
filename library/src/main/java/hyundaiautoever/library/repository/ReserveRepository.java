package hyundaiautoever.library.repository;

import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Reserve;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {

    List<Reserve> findAllByBook(Book book);

    Reserve findByWaitNumber(Integer waitNumber);

    @Query("SELECT r FROM Reserve r WHERE r.waitNumber > :waitNumber")
    List<Reserve> findReserveListByWaitNumber(@Param("waitNumber") Integer waitNumber);
}
