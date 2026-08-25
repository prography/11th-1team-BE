package org.example.knockin.member.repository;

import org.example.knockin.member.entity.MemberInterest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberInterestRepository extends JpaRepository<MemberInterest, Long>, MemberInterestRepositoryCustom {
    boolean existsBySenderIdAndReceiverIdAndIsDeletedIsFalse(Long senderId, Long receiverId);
}
