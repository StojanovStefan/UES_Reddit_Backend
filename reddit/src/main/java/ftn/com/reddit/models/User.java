package ftn.com.reddit.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "email"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @NotNull
    @Size(max = 50)
    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @NotNull
    @Size(max = 255)
    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @NotNull
    @Email
    @Size(max = 100)
    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @NotNull
    @Size(max = 100)
    @Column(name = "display_name", length = 100, nullable = false)
    private String display_name;

    @Column(name = "bio")
    private String bio;

    @Size(max = 255)
    @Column(name = "profile_image_url", length = 255)
    private String profile_image_url;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @NotNull
    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked = false;

    @NotNull
    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime created_at;


    public enum UserRole {
        USER,
        ADMIN,
        GROUP_ADMIN
    }

    @PrePersist
    protected void onCreate() {
        this.created_at = LocalDateTime.now();
    }




    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public @NotNull @Size(max = 50) String getUsername() {
        return username;
    }

    public void setUsername(@NotNull @Size(max = 50) String username) {
        this.username = username;
    }

    public @NotNull @Size(max = 255) String getPassword() {
        return password;
    }

    public void setPassword(@NotNull @Size(max = 255) String password) {
        this.password = password;
    }

    public @NotNull @Email @Size(max = 100) String getEmail() {
        return email;
    }

    public void setEmail(@NotNull @Email @Size(max = 100) String email) {
        this.email = email;
    }

    public @NotNull @Size(max = 100) String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(@NotNull @Size(max = 100) String display_name) {
        this.display_name = display_name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public @Size(max = 255) String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(@Size(max = 255) String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public @NotNull UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(@NotNull UserRole userRole) {
        this.userRole = userRole;
    }

    public @NotNull Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(@NotNull Boolean blocked) {
        isBlocked = blocked;
    }

    public @NotNull LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(@NotNull LocalDateTime created_at) {
        this.created_at = created_at;
    }

}