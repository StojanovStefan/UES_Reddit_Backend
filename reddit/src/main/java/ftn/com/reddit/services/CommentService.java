package ftn.com.reddit.services;

import ftn.com.reddit.models.Comment;
import ftn.com.reddit.models.CommentDto;
import ftn.com.reddit.models.Post;
import ftn.com.reddit.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private ICommentRepository commentRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IPostRepository postRepository;

    public List<CommentDto> getCommentsForPost(Integer postId) {
        List<Comment> comments = commentRepository.findByPost_PostIdAndParentCommentIsNull(postId);
        return comments.stream()
                .map(this::convertToDto)
                .toList();
    }

    private CommentDto convertToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setCommentId(comment.getCommentId());
        dto.setContent(comment.getContent());
        dto.setUsername(comment.getUser().getUsername());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setReplies(comment.getReplies().stream().map(this::convertToDto).toList());
        return dto;
    }

    public CommentDto addComment(Integer postId, String username, String content, Integer parentCommentId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());

        if (parentCommentId != null) {
            Comment parent = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
            comment.setParentComment(parent);
        }

        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }

}