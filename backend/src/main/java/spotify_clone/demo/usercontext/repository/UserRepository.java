package spotify_clone.demo.usercontext.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spotify_clone.demo.usercontext.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByEmail(String email);
}
