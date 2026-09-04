package org.example.knockin.agreement.repository.impl;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.knockin.agreement.entity.Agreement;
import org.example.knockin.agreement.entity.QAgreement;
import org.example.knockin.agreement.repository.AgreementRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;
import static org.example.knockin.agreement.entity.QAgreement.agreement;
import static org.example.knockin.agreement.entity.QAgreementLog.agreementLog;

@Repository
@RequiredArgsConstructor
public class AgreementRepositoryImpl implements AgreementRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Agreement> findByAgreements(List<Long> agreementId) {
        return jpaQueryFactory.selectFrom(agreement).where(agreement.id.in(agreementId)).fetch();
    }

    @Override
    public List<Agreement> findByAgreementsIsCurrentAndIsDeleted() {
        QAgreement subAgreement = new QAgreement("subAgreement");

        return jpaQueryFactory
                .selectFrom(agreement)
                .where(agreement.isDeleted.isFalse(),
                        agreement.id.eq(JPAExpressions.select(subAgreement.id)
                                        .from(agreementLog).join(agreementLog.agreement, subAgreement)
                                        .where(subAgreement.type.eq(agreement.type), agreementLog.isCurrent.isTrue()).orderBy(agreementLog.id.desc()).limit(1))).fetch();
    }
}