package ftn.com.reddit.controllers;

import ftn.com.reddit.models.CommentDto;
import ftn.com.reddit.models.User;
import ftn.com.reddit.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Integer postId) {
        List<CommentDto> comments = commentService.getCommentsForPost(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Integer postId,
            @RequestParam String content,
            @RequestParam(required = false) Integer parentCommentId,
            @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CommentDto comment = commentService.addComment(postId, user.getUsername(), content, parentCommentId);
        return ResponseEntity.ok(comment);
    }
}