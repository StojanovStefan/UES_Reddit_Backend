package ftn.com.reddit.controllers;

import ftn.com.reddit.models.User;
import ftn.com.reddit.services.LikeService;
import ftn.com.reddit.services.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private IUserRepository userRepository;

    // LIKE POST
    @PostMapping("/post/{postId}")
    public ResponseEntity<?> likePost(@PathVariable Integer postId) {
        User user = getAuthenticatedUser();
        if (user == null) return ResponseEntity.status(401).body("Unauthorized");

        String message = likeService.likePost(postId, user.getUsername());
        return ResponseEntity.ok(message);
    }

    // UNLIKE POST
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<?> unlikePost(@PathVariable Integer postId) {
        User user = getAuthenticatedUser();
        if (user == null) return ResponseEntity.status(401).body("Unauthorized");

        likeService.unlikePost(postId, user.getUsername());
        return ResponseEntity.ok("Like removed");
    }

    // LIKE COMMENT
    @PostMapping("/comment/{commentId}")
    public ResponseEntity<?> likeComment(@PathVariable Integer commentId) {
        User user = getAuthenticatedUser();
        if (user == null) return ResponseEntity.status(401).body("Unauthorized");

        String message = likeService.likeComment(commentId, user.getUsername());
        return ResponseEntity.ok(message);
    }

    // UNLIKE COMMENT
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> unlikeComment(@PathVariable Integer commentId) {
        User user = getAuthenticatedUser();
        if (user == null) return ResponseEntity.status(401).body("Unauthorized");

        likeService.unlikeComment(commentId, user.getUsername());
        return ResponseEntity.ok("Like removed");
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) return null;

        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) return user;

        return null;
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getLikesForPost(@PathVariable Integer postId) {
        User user = getAuthenticatedUser();
        if (user == null) return ResponseEntity.status(401).body("Unauthorized");

        int count = likeService.getLikesForPost(postId);

        boolean likedByUser = likeService.isPostLikedByUser(postId, user.getUsername());

        return ResponseEntity.ok(new LikeResponse(count, likedByUser));
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<?> getLikesForComment(@PathVariable Integer commentId) {
        User user = getAuthenticatedUser();
        if (user == null) return ResponseEntity.status(401).body("Unauthorized");

        int count = likeService.getLikesForComment(commentId);

        boolean likedByUser = likeService.isCommentLikedByUser(commentId, user.getUsername());

        return ResponseEntity.ok(new LikeResponse(count, likedByUser));
    }

    static class LikeResponse {
        public int count;
        public boolean likedByUser;

        public LikeResponse(int count, boolean likedByUser) {
            this.count = count;
            this.likedByUser = likedByUser;
        }
    }
}
