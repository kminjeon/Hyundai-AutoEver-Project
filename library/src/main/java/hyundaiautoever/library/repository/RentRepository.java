package hyundaiautoever.library.repository;

import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.model.dto.BookDto;
import hyundaiautoever.library.model.dto.RentDto;
import hyundaiautoever.library.model.dto.RentDto.GetRentDto;
import hyundaiautoever.library.model.entity.Book;
import hyundaiautoever.library.model.entity.Rent;
import hyundaiautoever.library.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static hyundaiautoever.library.model.dto.RentDto.*;

public interface RentRepository extends JpaRepository<Rent, Long>, RentRepositorySupport {
    @Query("SELECT new hyundaiautoever.library.model.dto.RentDto$GetRentDto(r) FROM Rent r WHERE r.user = :user AND r.returnDate IS NULL")
    Page<GetRentDto> findGetRentDtoByUser(Pageable pageable, @Param("user") User user);

    @Query("SELECT new hyundaiautoever.library.model.dto.RentDto$GetRentHistoryDto(r) FROM Rent r WHERE r.user = :user AND r.returnDate IS NOT NULL")
    Page<GetRentHistoryDto> findGetRentHistoryDtoByUser(Pageable pageable, @Param("user") User user);

    @Query("SELECT r FROM Rent r WHERE r.user = :user AND r.returnDate IS NULL")
    List<Rent> findByUser(@Param("user") User user);

    @Query("SELECT r FROM Rent r WHERE r.book = :book AND r.returnDate IS NULL")
    List<Rent> findByBook(@Param("book")Book book);

    @Query("SELECT r FROM Rent r WHERE r.user = :user AND r.book = :book AND r.returnDate IS NULL")
    Rent findByUserAndBook(@Param("user") User user, @Param("book") Book book);

    void deleteAllByUser(User user);

    void deleteAllByBook(Book book);

}
