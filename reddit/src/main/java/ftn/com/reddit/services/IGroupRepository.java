package ftn.com.reddit.services;

import ftn.com.reddit.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IGroupRepository extends JpaRepository<Group, Integer> {
    Optional<Group> findByName(String name);
}
