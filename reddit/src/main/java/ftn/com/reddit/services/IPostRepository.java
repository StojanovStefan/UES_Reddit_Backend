package ftn.com.reddit.services;

import ftn.com.reddit.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPostRepository extends JpaRepository<Post, Integer> {



}