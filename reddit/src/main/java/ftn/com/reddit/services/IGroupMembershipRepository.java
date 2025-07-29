package ftn.com.reddit.services;

import ftn.com.reddit.models.GroupMembership;
import ftn.com.reddit.models.GroupMembership.MembershipStatus;
import ftn.com.reddit.models.Group;
import ftn.com.reddit.models.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface IGroupMembershipRepository extends JpaRepository<GroupMembership, Integer> {

    Optional<GroupMembership> findByUserAndGroup(User user, Group group);

    List<GroupMembership> findByGroupAndStatus(Group group, MembershipStatus status);

    List<GroupMembership> findByUser(User user);

    boolean existsByGroupAndUserAndStatus(Group group, User user, MembershipStatus status);

    boolean existsByGroupAndUser(Group group, User user);
}
