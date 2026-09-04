package org.example.knockin.inquiry.repository;

import org.example.knockin.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>, InquiryRepositoryCustom {
    Optional<Inquiry> findByIdAndIsDeleted(Long id, Boolean isDeleted);
}