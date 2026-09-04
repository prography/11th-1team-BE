package org.example.knockin.meta.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.example.knockin.meta.entity.File;
import org.example.knockin.meta.entity.FileType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findFirstBySavedFileName(String savedFileName);
    List<File> findBySavedFileNameIn(Collection<String> savedFileNames);

    List<File> findBySavedFileName(String savedFileName);

    Optional<File> findBySavedFileNameAndType(String savedFileName, FileType type);
}