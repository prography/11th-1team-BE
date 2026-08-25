package org.example.knockin.authentication.repository;

import org.example.knockin.authentication.entity.Authentication;
import org.example.knockin.authentication.entity.AuthenticationType;
import org.example.knockin.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthenticationRepository extends JpaRepository<Authentication, Long>, AuthenticationRepositoryCustom {
    List<Authentication> findByMemberAndIsDeletedAndIsAccepted(Member member, Boolean isDeleted, Boolean isAccepted);

    List<Authentication> findByMemberAndIsDeletedAndIsAcceptedAndType(Member member, Boolean isDeleted, Boolean isAccepted, AuthenticationType type);
    Optional<Authentication> findFirstByMemberAndIsDeletedAndIsAcceptedAndTypeOrderByCreatedAtDesc(Member member, Boolean isDeleted, Boolean isAccepted, AuthenticationType type);
}