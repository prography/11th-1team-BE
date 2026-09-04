package org.example.knockin.meta.service.impl;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.global.exception.FileErrorCode;
import org.example.knockin.meta.service.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.S3Client;


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
            throw new BusinessException(FileErrorCode.FILE_EMPTY);
        }
        String type = file.getContentType();

        String originalFilename = file.getOriginalFilename();
        String extension = extractExtension(originalFilename,type);
        String key = "uploads/"+ UUID.randomUUID()+extension;

        PutObjectRequest req = PutObjectRequest.builder().bucket(bucket).key(key).contentType(type).build();

        s3Client.putObject(req, RequestBody.fromBytes(file.getBytes()));

        return publicUrl+"/"+key;
    }

    @Override
    public void deleteImage(String savedFileName) {
        if (savedFileName == null || savedFileName.isBlank()) {
            return;
        }

        String key = savedFileName;
        String prefix = publicUrl + "/";
        if (savedFileName.startsWith(prefix)) {
            key = savedFileName.substring(prefix.length());
        }

        DeleteObjectRequest req = DeleteObjectRequest.builder().bucket(bucket).key(key).build();
        s3Client.deleteObject(req);
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
