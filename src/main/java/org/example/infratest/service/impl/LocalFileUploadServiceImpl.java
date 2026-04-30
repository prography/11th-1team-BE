package org.example.infratest.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.infratest.service.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Profile("test")
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
