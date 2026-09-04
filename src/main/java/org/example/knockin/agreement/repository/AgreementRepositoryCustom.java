package org.example.knockin.agreement.repository;

import org.example.knockin.agreement.entity.Agreement;

import java.util.List;

public interface AgreementRepositoryCustom {
    List<Agreement> findByAgreements(List<Long> agreementId);
    List<Agreement> findByAgreementsIsCurrentAndIsDeleted();
}