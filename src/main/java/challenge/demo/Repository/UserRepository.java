package challenge.demo.Repository;

import challenge.demo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
