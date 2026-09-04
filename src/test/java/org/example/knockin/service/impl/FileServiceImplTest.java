package org.example.knockin.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import org.example.knockin.meta.entity.File;
import org.example.knockin.meta.entity.FileType;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.global.exception.FileErrorCode;
import org.example.knockin.meta.repository.FileRepository;
import org.example.knockin.meta.service.FileUploadService;
import org.example.knockin.meta.service.impl.FileServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
@DisplayName("파일 업로드 메타데이터 생성 서비스")
class FileServiceImplTest {

    @Mock
    private FileUploadService fileUploadService;

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileServiceImpl fileService;

    @Test
    @DisplayName("이미지 업로드가 성공하면 저장하지 않은 파일 엔티티를 생성한다")
    void uploadCreatesUnsavedFileEntity() throws IOException {
        MultipartFile multipartFile = mockMultipartFile("room.JPG");
        when(fileUploadService.uploadImage(multipartFile)).thenReturn("saved-room.jpg");

        File file = fileService.upload(multipartFile, FileType.ROOMMATE_BOARD_IMAGE);

        assertThat(file.getType()).isEqualTo(FileType.ROOMMATE_BOARD_IMAGE);
        assertThat(file.getOriginalFileName()).isEqualTo("room.JPG");
        assertThat(file.getSavedFileName()).isEqualTo("saved-room.jpg");
        assertThat(file.getFileExt()).isEqualTo("JPG");
    }

    @Test
    @DisplayName("이미지 업로드가 성공하면 파일 엔티티를 저장한다")
    void saveUploadsAndPersistsFileEntity() throws IOException {
        MultipartFile multipartFile = mockMultipartFile("room.JPG");
        when(fileUploadService.uploadImage(multipartFile)).thenReturn("saved-room.jpg");
        when(fileRepository.save(any(File.class))).thenAnswer(invocation -> invocation.getArgument(0));

        File file = fileService.save(multipartFile, FileType.ROOMMATE_BOARD_IMAGE);

        assertThat(file.getSavedFileName()).isEqualTo("saved-room.jpg");
        verify(fileRepository).save(file);
    }

    @Test
    @DisplayName("원본 파일명에 확장자가 없으면 실제 업로드를 요청하지 않고 예외가 발생한다")
    void uploadThrowsBeforeUploadWhenOriginalFileNameHasNoExtension() {
        MultipartFile multipartFile = mockMultipartFile("room");

        assertThatThrownBy(() -> fileService.upload(multipartFile, FileType.ROOMMATE_BOARD_IMAGE))
                .isInstanceOfSatisfying(BusinessException.class,
                        e -> assertThat(e.getErrorCode()).isEqualTo(FileErrorCode.FILE_EXTENSION_MISSING));

        verifyNoInteractions(fileUploadService);
    }

    @Test
    @DisplayName("보상 삭제 중 일부 파일 삭제가 실패해도 예외를 던지지 않고 다음 파일 삭제를 시도한다")
    void deleteAllQuietlySwallowsDeleteFailureAndContinues() throws IOException {
        File firstFile = createFile("first.jpg");
        File secondFile = createFile("second.jpg");
        doThrow(new IOException("delete failed")).when(fileUploadService).deleteImage("first.jpg");

        assertThatCode(() -> fileService.deleteAll(List.of(firstFile, secondFile)))
                .doesNotThrowAnyException();

        verify(fileUploadService).deleteImage("first.jpg");
        verify(fileUploadService).deleteImage("second.jpg");
    }

    private MultipartFile mockMultipartFile(String originalFileName) {
        MultipartFile file = org.mockito.Mockito.mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn(originalFileName);
        return file;
    }

    private File createFile(String savedFileName) {
        return File.builder()
                .type(FileType.ROOMMATE_BOARD_IMAGE)
                .originalFileName(savedFileName)
                .savedFileName(savedFileName)
                .fileExt("jpg")
                .build();
    }
}
