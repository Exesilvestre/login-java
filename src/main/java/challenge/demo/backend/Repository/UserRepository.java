package challenge.demo.backend.Repository;

import challenge.demo.backend.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
