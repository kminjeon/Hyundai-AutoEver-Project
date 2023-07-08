package hyundaiautoever.library.repository;


import hyundaiautoever.library.model.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ApplyRepository extends JpaRepository<Apply, Long>, ApplyRepositorySupport {


}
