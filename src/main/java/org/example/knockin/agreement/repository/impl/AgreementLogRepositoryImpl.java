package org.example.knockin.agreement.repository.impl;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.knockin.agreement.entity.AgreementLog;
import org.example.knockin.agreement.repository.AgreementLogRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import static org.example.knockin.agreement.entity.QAgreementLog.agreementLog;
import static org.example.knockin.agreement.entity.QAgreement.agreement;
import static org.example.knockin.agreement.entity.QAgreementType.agreementType;

@Repository
@RequiredArgsConstructor
public class AgreementLogRepositoryImpl implements AgreementLogRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<AgreementLog> findByAgreementLogIsCurrent(List<Long> agreementIds) {
        return jpaQueryFactory.selectFrom(agreementLog).join(agreementLog.agreement, agreement).where(agreement.id.in(agreementIds), agreementLog.isCurrent.eq(true)).fetch();
    }

    @Override
    public List<AgreementLog> findByAgreemnetIsCurrent(boolean isCurrent, Pageable pageable, Long agreementTypeId) {
        return jpaQueryFactory.selectFrom(agreementLog)
                .join(agreementLog.agreement, agreement).fetchJoin()
                .where(agreementLog.id.in(JPAExpressions.select(agreementLog.id)
                                        .from(agreementLog)
                                        .join(agreementLog.agreement, agreement)
                                        .join(agreement.type, agreementType)
                                        .where(agreementType.id.eq(agreementTypeId), agreement.isDeleted.eq(false))
                                        .offset(pageable.getOffset()).limit(pageable.getPageSize()))).fetch();
    }
}