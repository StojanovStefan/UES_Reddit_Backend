package ftn.com.reddit.services;

import ftn.com.reddit.models.Group;
import ftn.com.reddit.models.Post;
import ftn.com.reddit.models.PostDto;
import ftn.com.reddit.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private IGroupRepository groupRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IPostRepository postRepository;

    public Post createPost(PostDto postDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));

        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setPdfKey(postDto.getPdfKey());
        post.setUser(user);

        if (postDto.getGroupId() != null) {
            Group group = groupRepository.findById(postDto.getGroupId())
                    .orElseThrow(() -> new RuntimeException("Grupa nije pronađena."));
            post.setGroup(group);
        } else {
            post.setGroup(null); // eksplicitno ako želiš jasnoću
        }

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
