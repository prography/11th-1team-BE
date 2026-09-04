package org.example.knockin.inquiry.repository;

import org.example.knockin.inquiry.entity.InquiryCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryCategoryRepository extends JpaRepository<InquiryCategory, Long> {
    List<InquiryCategory> findAllByIsDeleted(Boolean isDeleted);
}