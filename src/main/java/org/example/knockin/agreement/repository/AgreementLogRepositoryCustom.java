package org.example.knockin.agreement.repository;

import org.example.knockin.agreement.entity.AgreementLog;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AgreementLogRepositoryCustom {
    List<AgreementLog> findByAgreementLogIsCurrent(List<Long> agreementIds);
    List<AgreementLog> findByAgreemnetIsCurrent(boolean isCurrent, Pageable pageable, Long agreementTypeId);
}