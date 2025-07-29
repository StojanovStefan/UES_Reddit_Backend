package ftn.com.reddit.services;

import ftn.com.reddit.models.Comment;
import ftn.com.reddit.models.Like;
import ftn.com.reddit.models.Post;
import ftn.com.reddit.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private ILikeRepository likeRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private ICommentRepository commentRepository;

    public String likePost(Integer postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);
        if (existingLike.isPresent()) {
            return "User already liked this post";
        }

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);

        return "Post liked successfully";
    }

    public String unlikePost(Integer postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);
        if (existingLike.isEmpty()) {
            return "Like not found";
        }

        likeRepository.delete(existingLike.get());
        return "Post unliked successfully";
    }

    public String likeComment(Integer commentId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        Optional<Like> existingLike = likeRepository.findByUserAndComment(user, comment);
        if (existingLike.isPresent()) {
            return "User already liked this comment";
        }

        Like like = new Like();
        like.setUser(user);
        like.setComment(comment);
        likeRepository.save(like);

        return "Comment liked successfully";
    }

    public String unlikeComment(Integer commentId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        Optional<Like> existingLike = likeRepository.findByUserAndComment(user, comment);
        if (existingLike.isEmpty()) {
            return "Like not found";
        }

        likeRepository.delete(existingLike.get());
        return "Comment unliked successfully";
    }

    public int getLikesForPost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return likeRepository.countByPost(post);
    }

    public int getLikesForComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        return likeRepository.countByComment(comment);
    }

    public boolean isPostLikedByUser(Integer postId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        return likeRepository.findByUserAndPost(user, post).isPresent();
    }

    public boolean isCommentLikedByUser(Integer commentId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));

        return likeRepository.findByUserAndComment(user, comment).isPresent();
    }

}
