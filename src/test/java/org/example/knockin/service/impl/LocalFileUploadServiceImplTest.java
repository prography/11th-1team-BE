package org.example.knockin.service.impl;
 
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.global.exception.FileErrorCode;
import org.example.knockin.meta.service.impl.LocalFileUploadServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
 
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class LocalFileUploadServiceImplTest {
 
    @InjectMocks
    private LocalFileUploadServiceImpl localFileUploadService;
 
    private String uploadedFileName;
 
    @AfterEach
    void cleanUp() throws IOException {
        if (uploadedFileName != null) {
            localFileUploadService.deleteImage(uploadedFileName);
        }
    }
 
    @Test
    @DisplayName("로컬 파일 업로드 성공 테스트")
    void uploadImage_Success() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
 
        uploadedFileName = localFileUploadService.uploadImage(file);
 
        assertThat(uploadedFileName).isNotBlank();
        Path filePath = Paths.get(System.getProperty("user.dir") + "/local_uploads/", uploadedFileName);
        assertThat(Files.exists(filePath)).isTrue();
    }
 
    @Test
    @DisplayName("빈 파일 업로드 시 예외 발생 테스트")
    void uploadImage_ThrowsWhenFileEmpty() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "image",
                "",
                "image/jpeg",
                new byte[0]
        );
 
        assertThatThrownBy(() -> localFileUploadService.uploadImage(emptyFile))
                .isInstanceOf(BusinessException.class)
                .satisfies(e -> assertThat(((BusinessException) e).getErrorCode()).isEqualTo(FileErrorCode.FILE_EMPTY));
    }
 
    @Test
    @DisplayName("null 파일 업로드 시 예외 발생 테스트")
    void uploadImage_ThrowsWhenFileNull() {
        assertThatThrownBy(() -> localFileUploadService.uploadImage(null))
                .isInstanceOf(BusinessException.class)
                .satisfies(e -> assertThat(((BusinessException) e).getErrorCode()).isEqualTo(FileErrorCode.FILE_EMPTY));
    }
 
    @Test
    @DisplayName("파일 삭제 성공 테스트")
    void deleteImage_Success() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test-delete.png",
                "image/png",
                "delete me".getBytes()
        );
        String fileName = localFileUploadService.uploadImage(file);
 
        localFileUploadService.deleteImage(fileName);
 
        Path filePath = Paths.get(System.getProperty("user.dir") + "/local_uploads/", fileName);
        assertThat(Files.exists(filePath)).isFalse();
    }
 
    @Test
    @DisplayName("유효하지 않은 파일 경로 삭제 시 예외 발생 테스트")
    void deleteImage_ThrowsWhenPathInvalid() {
        assertThatThrownBy(() -> localFileUploadService.deleteImage("../outside.png"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid file path");
    }
}
