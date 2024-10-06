package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "https://loud-cooks-ring.loca.lt/")
@RequestMapping("/api/profile")
public class ProfileController {

    private final String AVATAR_DIR = "avatars/";

    @Autowired
    private ProfileRepository profileRepository;

    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                               @RequestParam("username") String username) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a file to upload.");
        }

        try {
            // Tạo thư mục avatars nếu chưa tồn tại
            File directory = new File(AVATAR_DIR);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Mã hóa tên file
            String encodedFileName = Base64.getEncoder().encodeToString(file.getOriginalFilename().getBytes());
            Path path = Paths.get(AVATAR_DIR + encodedFileName);
            Files.write(path, file.getBytes());

            // Lưu thông tin profile
            Profile profile = profileRepository.findByUsername(username);
            if (profile == null) {
                profile = new Profile(username, path.toString());
            } else {
                profile.setAvatarPath(path.toString());
            }

            profileRepository.save(profile);
            return ResponseEntity.ok("Avatar uploaded successfully: " + encodedFileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading avatar: " + e.getMessage());
        }
    }

    @GetMapping("/avatar/{username}")
    public ResponseEntity<String> getAvatar(@PathVariable String username) {
        Profile profile = profileRepository.findByUsername(username);
        if (profile != null && profile.getAvatarPath() != null) {
            return ResponseEntity.ok("Avatar path: " + profile.getAvatarPath());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Avatar not found for user: " + username);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Profile> getProfile(@PathVariable String username) {
        Profile profile = profileRepository.findByUsername(username);
        if (profile != null) {
            return ResponseEntity.ok(profile);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

