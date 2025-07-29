package ftn.com.reddit.models;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "user_groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "name",nullable = false, unique = true)
    private String name;

    @Column(name = "pdf_key", nullable = false)
    private String pdfKey;

    @ManyToOne
    @JoinColumn(name = "administrator_id", nullable = false)
    private User admin;

    @OneToMany(mappedBy = "group")
    private List<Post> posts;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPdfKey() {
        return pdfKey;
    }

    public void setPdfKey(String pdfKey) {
        this.pdfKey = pdfKey;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}