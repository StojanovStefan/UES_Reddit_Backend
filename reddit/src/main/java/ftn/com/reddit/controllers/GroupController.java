package ftn.com.reddit.controllers;

import ftn.com.reddit.models.Group;
import ftn.com.reddit.models.User;
import ftn.com.reddit.services.GroupService;
import ftn.com.reddit.services.IGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private IGroupRepository groupRepository;


    @GetMapping
    public ResponseEntity<?> getAllGroups() {
        return ResponseEntity.ok(groupRepository.findAll());
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getGroupDetails(@PathVariable Integer groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Grupa nije pronađena."));
        return ResponseEntity.ok(group);
    }

    @PostMapping
    public ResponseEntity<?> createGroup(
            @RequestParam("name") String name,
            @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Niste ulogovani.");
        }

        Object principal = auth.getPrincipal();
        if (!(principal instanceof User user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nepoznat korisnik.");
        }

        try {
            Group group = groupService.createGroup(user.getUsername(), name, pdfFile);
            return ResponseEntity.ok(group);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška pri kreiranju grupe.");
        }
    }
}