package personal.project.myfavariteplayers.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.project.myfavariteplayers.Entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
