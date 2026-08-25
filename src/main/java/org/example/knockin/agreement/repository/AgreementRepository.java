package org.example.knockin.agreement.repository;

import org.example.knockin.agreement.entity.Agreement;
import org.example.knockin.agreement.entity.AgreementType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AgreementRepository extends JpaRepository<Agreement, Long>, AgreementRepositoryCustom {
    List<Agreement> findAllByIsDeleted(Boolean isDeleted);

    Collection<Agreement> findByType(AgreementType type);
}