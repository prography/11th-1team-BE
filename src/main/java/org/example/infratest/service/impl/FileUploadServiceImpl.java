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

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Profile("prod")
public class FileUploadServiceImpl implements FileUploadService {
    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.public-url}")
    private String publicUrl;

    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String type = file.getContentType();

        String originalFilename = file.getOriginalFilename();
        String extension = extractExtension(originalFilename,type);
        String key = "uploads/"+ UUID.randomUUID()+extension;

        PutObjectRequest req = PutObjectRequest.builder().bucket(bucket).key(key).contentType(type).build();

        s3Client.putObject(req, RequestBody.fromBytes(file.getBytes()));

        return publicUrl+"/"+key;
    }

    private String extractExtension(String originalFileName,String type){
        if (originalFileName != null && originalFileName.contains(".")) {
            return originalFileName.substring(originalFileName.lastIndexOf('.')).toLowerCase();
        }

        return switch (type) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            case "image/gif" -> ".gif";
            default -> "";
        };
    }
}
