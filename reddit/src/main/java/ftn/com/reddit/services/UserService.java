package ftn.com.reddit.services;


import ftn.com.reddit.models.User;
import ftn.com.reddit.models.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(UserDto dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already taken.");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already taken.");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setDisplay_name(dto.getDisplay_name());
        user.setBio(dto.getBio());
        user.setProfile_image_url(dto.getProfile_image_url());
        user.setUserRole(User.UserRole.USER);
        user.setBlocked(false);
        user.setCreated_at(LocalDateTime.now());

        return userRepository.save(user);
    }
}
