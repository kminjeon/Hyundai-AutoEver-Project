package hyundaiautoever.library.repository;

import hyundaiautoever.library.model.dto.UserDto;
import hyundaiautoever.library.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositorySupport {

    User findByName(String name);

    Optional<User> findByPersonalId(String personalId);

    boolean existsByPersonalId(String personalId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    User findByPersonalIdAndPassword(String personalId, String password);

}
