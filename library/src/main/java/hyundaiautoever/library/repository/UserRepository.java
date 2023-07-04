package hyundaiautoever.library.repository;

import hyundaiautoever.library.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);

    Optional<User> findByPersonalId(String personalId);

    boolean existsByPersonalId(String personalId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    User findByPersonalIdAndPassword(String personalId, String password);

}
