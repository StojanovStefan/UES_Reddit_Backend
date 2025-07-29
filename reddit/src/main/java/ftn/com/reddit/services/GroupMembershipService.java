package ftn.com.reddit.services;

import ftn.com.reddit.models.Group;
import ftn.com.reddit.models.GroupMembership;
import ftn.com.reddit.models.User;
import ftn.com.reddit.services.IGroupMembershipRepository;
import ftn.com.reddit.services.IGroupRepository;
import ftn.com.reddit.services.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupMembershipService {

    @Autowired
    private IGroupMembershipRepository membershipRepository;

    @Autowired
    private IGroupRepository groupRepository;

    @Autowired
    private IUserRepository userRepository;

    // Provera da li je korisnik clan grupe
    public boolean isMember(Integer groupId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Korisnik ne postoji."));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Grupa ne postoji."));
        return membershipRepository.existsByGroupAndUserAndStatus(group, user, GroupMembership.MembershipStatus.APPROVED);
    }

    // Metod za slanje zahteva ili automatsko dodavanje ako je admin
    public void requestMembership(Integer groupId, String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Korisnik ne postoji.");
        }
        User user = userOpt.get();

        Optional<Group> groupOpt = groupRepository.findById(groupId);
        if (groupOpt.isEmpty()) {
            throw new RuntimeException("Grupa ne postoji.");
        }
        Group group = groupOpt.get();

        // Ako je korisnik admin grupe, odmah ga odobrimo kao clana
        if (group.getAdmin().getUsername().equals(user.getUsername())) {
            // Proveri da li je već član (koristi metodu koja prima entitete)
            boolean alreadyMember = membershipRepository.existsByGroupAndUserAndStatus(
                    group, user, GroupMembership.MembershipStatus.APPROVED
            );

            if (alreadyMember) {
                throw new RuntimeException("Korisnik je već član grupe.");
            }

            GroupMembership membership = new GroupMembership();
            membership.setGroup(group);
            membership.setUser(user);
            membership.setStatus(GroupMembership.MembershipStatus.APPROVED);
            membershipRepository.save(membership);
            return;
        }

        // Ako nije admin, proveri da li je već član
        boolean alreadyMember = membershipRepository.existsByGroupAndUserAndStatus(group, user, GroupMembership.MembershipStatus.APPROVED);
        if (alreadyMember) {
            throw new RuntimeException("Korisnik je već član grupe.");
        }

        // Proveri da li postoji vec zahtev u pending statusu
        boolean pendingRequest = membershipRepository.existsByGroupAndUserAndStatus(group, user, GroupMembership.MembershipStatus.PENDING);
        if (pendingRequest) {
            throw new RuntimeException("Zahtev za članstvo je već poslat.");
        }


        // Napravi novi zahtev sa statusom PENDING
        GroupMembership membership = new GroupMembership();
        membership.setGroup(group);
        membership.setUser(user);
        membership.setStatus(GroupMembership.MembershipStatus.PENDING);
        membershipRepository.save(membership);
    }

    public void approveMembership(Integer membershipId, String approverUsername) {
        GroupMembership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Članstvo ne postoji."));

        // Proveri da li je approver admin grupe
        if (!membership.getGroup().getAdmin().getUsername().equals(approverUsername)) {
            throw new RuntimeException("Nemate ovlašćenje da odobrite članstvo.");
        }


        membership.setStatus(GroupMembership.MembershipStatus.APPROVED);
        membershipRepository.save(membership);
    }

    public void rejectMembership(Integer membershipId, String approverUsername) {
        GroupMembership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Članstvo ne postoji."));

        // Proveri da li je approver admin grupe
        if (!membership.getGroup().getAdmin().getUsername().equals(approverUsername)) {
            throw new RuntimeException("Nemate ovlašćenje da odbijete članstvo.");
        }


        membership.setStatus(GroupMembership.MembershipStatus.REJECTED);
        membershipRepository.save(membership);
    }

    public List<GroupMembership> getPendingRequests(Integer groupId, String requesterUsername) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Grupa ne postoji."));

        // Samo admini mogu da vide pending zahteve
        if (!group.getAdmin().getUsername().equals(requesterUsername)) {
            throw new RuntimeException("Nemate ovlašćenje da vidite zahteve.");
        }

        return membershipRepository.findByGroupAndStatus(group, GroupMembership.MembershipStatus.PENDING);
    }

}
