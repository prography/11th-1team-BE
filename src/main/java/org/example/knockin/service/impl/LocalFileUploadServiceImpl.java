package org.example.knockin.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.knockin.service.FileUploadService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Profile("!prod")
public class LocalFileUploadServiceImpl implements FileUploadService {
    private final String uploadDir = System.getProperty("user.dir") + "/local_uploads/";

    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase();
        }

        String fileName = UUID.randomUUID() + extension;
        Path filePath = Paths.get(uploadDir, fileName);
        file.transferTo(filePath.toFile());

        return fileName;
    }
}
