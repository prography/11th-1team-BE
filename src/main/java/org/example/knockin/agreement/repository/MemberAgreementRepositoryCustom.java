package org.example.knockin.agreement.repository;

import org.example.knockin.agreement.entity.AgreementLog;
import org.example.knockin.agreement.entity.MemberAgreement;
import org.example.knockin.member.entity.Member;

import java.util.List;

public interface MemberAgreementRepositoryCustom {
    List<MemberAgreement> findByMemberAndAgreementLogNotIn(Member member, List<AgreementLog> skipList);
}
