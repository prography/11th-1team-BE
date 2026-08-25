package org.example.knockin.member.repository;

import org.example.knockin.member.entity.BasicInformationFile;
import org.example.knockin.member.entity.BasicInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BasicInformationFileRepository extends JpaRepository<BasicInformationFile, Long> {
    Optional<BasicInformationFile> findByBasicInformation(BasicInformation basicInformation);
}