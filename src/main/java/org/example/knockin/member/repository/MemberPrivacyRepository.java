package org.example.knockin.member.repository;

import org.example.knockin.member.entity.Member;
import org.example.knockin.member.entity.MemberPrivacy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberPrivacyRepository extends JpaRepository<MemberPrivacy, Long> {
    List<MemberPrivacy> findByMember(Member member);

    List<MemberPrivacy> findByMemberId(Long memberId);
}