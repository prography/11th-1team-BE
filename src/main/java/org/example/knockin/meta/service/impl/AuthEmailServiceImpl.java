package org.example.knockin.meta.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.knockin.meta.dto.AuthEmailDeleteDto;
import org.example.knockin.meta.dto.AuthEmailListDto;
import org.example.knockin.meta.dto.AuthEmailModifyDto;
import org.example.knockin.meta.dto.AuthEmailSaveDto;
import org.example.knockin.meta.entity.AuthEmail;
import org.example.knockin.global.exception.AuthEmailErrorCode;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.meta.repository.AuthEmailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthEmailServiceImpl {
    private final AuthEmailRepository authEmailRepository;

    public AuthEmailListDto.Response findAuthEmailList() {
        List<AuthEmailListDto.Response.AuthEmailInfo> authEmailInfoList = authEmailRepository.findByIsDeletedFalse().stream().map(item ->
                AuthEmailListDto.Response.AuthEmailInfo.builder().id(item.getId()).domain(item.getDomain()).name(item.getName()).type(item.getDtype()).build()).toList();
        return AuthEmailListDto.Response.builder().authEmailInfoList(authEmailInfoList).build();
    }

    @Transactional
    public AuthEmailSaveDto.Response saveAuthEmail(AuthEmailSaveDto.Request request) {
        if (authEmailRepository.existsByDomainAndIsDeletedFalse(request.getDomain())) {
            throw new BusinessException(AuthEmailErrorCode.AUTH_EMAIL_DUPLICATE_DOMAIN);
        }
        authEmailRepository.save(AuthEmail.builder().domain(request.getDomain()).name(request.getName()).dtype(request.getType()).build());
        return AuthEmailSaveDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public AuthEmailModifyDto.Response modifyAuthEmail(AuthEmailModifyDto.Request request) {
        AuthEmail authEmail = authEmailRepository.findByIdAndIsDeletedFalse(request.getId()).orElseThrow(() -> new BusinessException(AuthEmailErrorCode.AUTH_EMAIL_NOT_FOUND));
        if (authEmailRepository.existsByDomainAndIsDeletedFalseAndIdNot(request.getDomain(), request.getId())) {
            throw new BusinessException(AuthEmailErrorCode.AUTH_EMAIL_DUPLICATE_DOMAIN);
        }
        authEmail.modifyAuthEmail(request);
        return AuthEmailModifyDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public AuthEmailDeleteDto.Response deleteAuthEmail(Long id) {
        AuthEmail authEmail = authEmailRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new BusinessException(AuthEmailErrorCode.AUTH_EMAIL_NOT_FOUND));
        authEmail.delete();
        return AuthEmailDeleteDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }
}
