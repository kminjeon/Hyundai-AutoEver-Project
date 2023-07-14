package hyundaiautoever.library.repository;


import hyundaiautoever.library.model.dto.ApplyDto;
import hyundaiautoever.library.model.dto.ApplyDto.GetApplyDto;
import hyundaiautoever.library.model.dto.ApplyDto.GetApplyPage;
import hyundaiautoever.library.model.entity.Apply;
import hyundaiautoever.library.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface ApplyRepository extends JpaRepository<Apply, Long>, ApplyRepositorySupport {

    @Query("SELECT new hyundaiautoever.library.model.dto.ApplyDto$GetApplyDto(a) FROM Apply a WHERE a.user = :user")
    Page<GetApplyDto> findByUser(Pageable pageable, @Param("user") User user);

    void deleteAllByUser(User user);

}
