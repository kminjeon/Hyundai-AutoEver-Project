package hyundaiautoever.library.repository;

import hyundaiautoever.library.model.dto.ReserveDto;
import hyundaiautoever.library.model.dto.ReserveDto.GetReserveDto;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Reserve;
import hyundaiautoever.library.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReserveRepository extends JpaRepository<Reserve, Long>, ReserveRepositorySupport {

    List<Reserve> findAllByBook(Book book);

    Optional<Reserve> findByUserAndBook(User user, Book book);

    Reserve findByWaitNumberAndBook(Integer waitNumber, Book book);

    @Query("SELECT r FROM Reserve r WHERE r.waitNumber > :waitNumber")
    List<Reserve> findReserveListByWaitNumber(@Param("waitNumber") Integer waitNumber);

    @Query("SELECT new hyundaiautoever.library.model.dto.ReserveDto$GetReserveDto(r)  FROM Reserve r WHERE r.user = :user")
    Page<GetReserveDto> findPageByUser(Pageable pageable, @Param("user") User user);

    List<Reserve> findByUser(User user);

    void deleteAllByUser(User user);
    void deleteAllByBook(Book book);

    List<Reserve> findAllByBookAndWaitNumberGreaterThan(Book book, Integer waitNumber);
}
