package org.example.knockin.inquiry.repository;

import org.example.knockin.inquiry.entity.InquiryComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryCommentRepository extends JpaRepository<InquiryComment, Long> {
}