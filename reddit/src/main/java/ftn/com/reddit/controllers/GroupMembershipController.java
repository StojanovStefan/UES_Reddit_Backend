package ftn.com.reddit.controllers;
import ftn.com.reddit.models.GroupMembership;
import ftn.com.reddit.services.GroupMembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-memberships")
public class GroupMembershipController {

    @Autowired
    private GroupMembershipService membershipService;

    private String getUsernameFromAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (principal instanceof ftn.com.reddit.models.User user) {
            return user.getUsername();
        } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            return userDetails.getUsername();
        } else if (principal instanceof String username) {
            return username;
        } else {
            throw new RuntimeException("Korisnik nije pronađen.");
        }
    }

    @GetMapping("/status/{groupId}")
    public ResponseEntity<?> checkMembershipStatus(@PathVariable Integer groupId) {
        String username = getUsernameFromAuthentication();
        boolean isMember = membershipService.isMember(groupId, username);
        return ResponseEntity.ok(isMember);
    }

    @PostMapping("/request/{groupId}")
    public ResponseEntity<?> requestMembership(@PathVariable Integer groupId) {
        String username = getUsernameFromAuthentication();

        try {
            membershipService.requestMembership(groupId, username);
            return ResponseEntity.ok("Zahtev za članstvo poslat.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/approve/{membershipId}")
    public ResponseEntity<?> approveMembership(@PathVariable Integer membershipId) {
        String username = getUsernameFromAuthentication();

        try {
            membershipService.approveMembership(membershipId, username);
            return ResponseEntity.ok("Zahtev je odobren.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PostMapping("/reject/{membershipId}")
    public ResponseEntity<?> rejectMembership(@PathVariable Integer membershipId) {
        String username = getUsernameFromAuthentication();

        try {
            membershipService.rejectMembership(membershipId, username);
            return ResponseEntity.ok("Zahtev je odbijen.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @GetMapping("/pending/{groupId}")
    public ResponseEntity<?> getPendingRequests(@PathVariable Integer groupId) {
        String username = getUsernameFromAuthentication();

        try {
            List<GroupMembership> requests = membershipService.getPendingRequests(groupId, username);
            return ResponseEntity.ok(requests);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}