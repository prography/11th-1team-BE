package org.example.knockin.agreement.repository;

import org.example.knockin.agreement.entity.MemberAgreement;
import org.example.knockin.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberAgreementRepository extends JpaRepository<MemberAgreement, Long>, MemberAgreementRepositoryCustom {
    List<MemberAgreement> findByMember(Member member);
}