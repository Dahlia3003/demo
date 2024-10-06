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
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/api")
public class FileUploadController {

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a file to upload.");
        }

        try {
            // Tạo thư mục uploads nếu chưa tồn tại
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Lưu file vào thư mục uploads
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, file.getBytes());

            return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file: " + e.getMessage());
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<String>> listFiles() {
        File folder = new File(UPLOAD_DIR);
        File[] files = folder.listFiles();
        List<String> fileNames = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                fileNames.add(file.getName());
            }
        }
        return ResponseEntity.ok(fileNames);
    }
}
