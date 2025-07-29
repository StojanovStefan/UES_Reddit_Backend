package ftn.com.reddit.services;

import ftn.com.reddit.models.Group;
import ftn.com.reddit.models.User;
import ftn.com.reddit.services.IGroupRepository;
import ftn.com.reddit.services.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GroupService {

    @Autowired
    private IGroupRepository groupRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private MinioService minioService;  // pretpostavljam da koristiš Minio za upload fajlova

    public Group createGroup(String username, String name, MultipartFile pdfFile) {
        // Pronađi korisnika po username
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));

        // Upload pdf fajla ako postoji
        String pdfKey = null;
        try {
            if (pdfFile != null && !pdfFile.isEmpty()) {
                pdfKey = minioService.uploadFile(pdfFile);
            }
        } catch (Exception e) {
            throw new RuntimeException("Greška pri uploadu PDF fajla.", e);
        }

        // Kreiraj i sačuvaj grupu
        Group group = new Group();
        group.setName(name);
        group.setPdfKey(pdfKey);
        group.setAdmin(creator);

        return groupRepository.save(group);
    }
}
