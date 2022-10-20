package personal.project.myfavariteplayers.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.project.myfavariteplayers.Entity.Board;
import personal.project.myfavariteplayers.Entity.User;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByIdAndUser(Long id, User user);

    List<Board> findAllByUser(User user);
}
