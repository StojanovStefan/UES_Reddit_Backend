package ftn.com.reddit.services;

import ftn.com.reddit.models.Comment;
import ftn.com.reddit.models.Like;
import ftn.com.reddit.models.Post;
import ftn.com.reddit.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ILikeRepository extends JpaRepository<Like, Integer> {

    Optional<Like> findByUserAndPost(User user, Post post);

    Optional<Like> findByUserAndComment(User user, Comment comment);

    int countByPost(Post post);

    int countByComment(Comment comment);

}