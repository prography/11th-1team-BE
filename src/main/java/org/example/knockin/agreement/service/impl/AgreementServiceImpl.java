package org.example.knockin.agreement.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.knockin.agreement.dto.BoTermsListDto;
import org.example.knockin.agreement.dto.BoTypeTermsListDto;
import org.example.knockin.agreement.entity.Agreement;
import org.example.knockin.agreement.entity.AgreementLog;
import org.example.knockin.agreement.entity.AgreementType;
import org.example.knockin.global.exception.AgreementErrorCode;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.agreement.repository.AgreementLogRepository;
import org.example.knockin.agreement.repository.AgreementRepository;
import org.example.knockin.agreement.repository.AgreementTypeRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgreementServiceImpl {
    private final AgreementRepository agreementRepository;
    private final AgreementLogRepository agreementLogRepository;
    private final AgreementTypeRepository agreementTypeRepository;

    @Transactional
    public Agreement saveAgreement(Agreement agreement) {
        Agreement agreementEntity = agreementRepository.save(agreement);
        agreementLogRepository.save(AgreementLog.builder().agreement(agreementEntity).isCurrent(true).build());
        return agreementEntity;
    }

    public AgreementType findAgreementType(Long agreementId) {
        return agreementRepository.findById(agreementId).orElseThrow(() -> new BusinessException(AgreementErrorCode.AGREEMENT_NOT_FOUNT)).getType();
    }

    @Transactional
    public Agreement modifyTemporaryAgreement(Agreement agreement) {
        Agreement agreementEntity = agreementRepository.save(agreement);
        agreementLogRepository.save(AgreementLog.builder().agreement(agreement).isCurrent(false).build());
        return agreementEntity;
    }

    @Transactional
    public Agreement modifyAgreement(Agreement agreement, Long agreementId) {
        Agreement agreementEntity = agreementRepository.findById(agreementId).orElseThrow(() -> new BusinessException(AgreementErrorCode.AGREEMENT_NOT_FOUNT));
        agreementLogRepository.findByAgreementIn(agreementRepository.findByType(agreementEntity.getType())).forEach(AgreementLog::clearCurrent);
        agreementEntity.modifyAgreement(agreement);
        agreementLogRepository.findById(agreementEntity.getId()).orElseThrow(() -> new BusinessException(AgreementErrorCode.AGREEMENT_LOG_NOT_FOUNT)).enableCurrent();
        return agreementEntity;
    }

    public List<BoTermsListDto.Response.TermsItem> findAgreementList(Pageable pageable, Long agreementTypeId) {
        return agreementLogRepository.findByAgreemnetIsCurrent(true, pageable, agreementTypeId).stream().map(item ->
                BoTermsListDto.Response.TermsItem.builder().title(item.getAgreement().getTitle()).createAt(item.getAgreement().getCreatedAt()).id(item.getAgreement().getId()).isCurrent(item.getIsCurrent()).build()).toList();
    }

    public Agreement findAgreement(Long id) {
        return agreementLogRepository.findById(id).orElseThrow(() -> new BusinessException(AgreementErrorCode.AGREEMENT_NOT_FOUNT)).getAgreement();
    }

    @Transactional
    public Agreement deleteAgreement(Long id) {
        Agreement agreement = agreementRepository.findById(id).orElseThrow(() -> new BusinessException(AgreementErrorCode.AGREEMENT_NOT_FOUNT));
        agreement.deleteAgreement();
        return agreement;
    }

    public AgreementType findAgreementTypeById(Long id) {
        return agreementTypeRepository.findById(id).orElseThrow(() -> new BusinessException(AgreementErrorCode.AGREEMENT_TYPE_NOT_FOUNT));
    }

    public List<BoTypeTermsListDto.Response.TermsTypeItem> findTypeTermsList() {
        return agreementTypeRepository.findAll().stream().map(item ->
                BoTypeTermsListDto.Response.TermsTypeItem.builder().id(item.getId()).title(item.getName()).createAt(item.getCreatedAt()).build()).toList();
    }

    @Transactional
    public AgreementType saveTermType(AgreementType agreementType) {
        return agreementTypeRepository.save(agreementType);
    }

    @Transactional
    public AgreementType deleteTermType(Long termTypeId) {
        AgreementType agreementType = agreementTypeRepository.findById(termTypeId).orElseThrow(() -> new BusinessException(AgreementErrorCode.AGREEMENT_TYPE_NOT_FOUNT));
        agreementType.deleteAgreementType();
        return agreementType;
    }

    public List<Agreement> findByAgreementsIsCurrentAndIsDeleted() {
        return agreementRepository.findByAgreementsIsCurrentAndIsDeleted();
    }

    public List<AgreementLog> findByAgreementLogIsCurrent(List<Long> agreementIds) {
        return agreementLogRepository.findByAgreementLogIsCurrent(agreementIds);
    }
}
