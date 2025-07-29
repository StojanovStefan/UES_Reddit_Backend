package ftn.com.reddit.services;
import ftn.com.reddit.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ICommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByPost_PostIdAndParentCommentIsNull(Integer postId);
}