package personal.project.myfavariteplayers.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import personal.project.myfavariteplayers.Entity.RefreshToken;
import personal.project.myfavariteplayers.Entity.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);
}
