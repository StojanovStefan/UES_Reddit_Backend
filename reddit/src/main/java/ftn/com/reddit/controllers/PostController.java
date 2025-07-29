package ftn.com.reddit.controllers;

import ftn.com.reddit.models.Post;
import ftn.com.reddit.models.PostDto;
import ftn.com.reddit.models.User;
import ftn.com.reddit.services.MinioService;
import ftn.com.reddit.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private MinioService minioService;

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        try {
            List<Post> posts = postService.getAllPosts();

            List<PostDto> postDtos = posts.stream().map(post -> {
                PostDto dto = new PostDto();
                dto.setPostId(post.getPostId());
                dto.setContent(post.getContent());
                dto.setPdfKey(post.getPdfKey());
                dto.setUsername(post.getUser().getUsername());
                return dto;
            }).toList();

            return ResponseEntity.ok(postDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška pri učitavanju postova.");
        }
    }

    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestParam("content") String content,
            @RequestParam("pdfFile") MultipartFile pdfFile,
            @RequestParam(value = "groupId", required = false) Integer groupId) {

        if (content == null || content.isEmpty() || pdfFile == null || pdfFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Sadržaj i PDF fajl su obavezni.");
        }

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Niste ulogovani.");
            }

            Object principal = authentication.getPrincipal();
            if (!(principal instanceof User)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nepoznat korisnik.");
            }

            User user = (User) principal;
            String pdfFileUrl = minioService.uploadFile(pdfFile);

            PostDto postDto = new PostDto();
            postDto.setContent(content);
            postDto.setPdfKey(pdfFileUrl);
            postDto.setGroupId(groupId);

            postService.createPost(postDto, user.getUsername());

            return ResponseEntity.ok("Post uspešno kreiran.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška pri kreiranju posta.");
        }
    }
}
