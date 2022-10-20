package personal.project.myfavariteplayers.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.project.myfavariteplayers.Entity.User;


@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    User findByUseridAndUserpwd(String userid, String userpwd);
    User findByUserid(String userid);
}
