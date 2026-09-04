package org.example.knockin.meta.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.knockin.meta.dto.FaqAllListDto;
import org.example.knockin.meta.dto.FaqDeleteDto;
import org.example.knockin.meta.dto.FaqDto;
import org.example.knockin.meta.dto.FaqListDto;
import org.example.knockin.meta.dto.FaqModifyDto;
import org.example.knockin.meta.dto.FaqSaveDto;
import org.example.knockin.meta.entity.Faq;
import org.example.knockin.member.entity.Member;
import org.example.knockin.global.exception.AuthErrorCode;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.global.exception.FaqErrorCode;
import org.example.knockin.meta.repository.FaqRepository;
import org.example.knockin.member.service.impl.MemberServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl {
    private final FaqRepository faqRepository;
    private final MemberServiceImpl memberService;

    @Transactional
    public FaqSaveDto.Response saveFaq(FaqSaveDto.Request request, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        if(!faqRepository.findBySort(request.getSort()).isEmpty()) throw new BusinessException(FaqErrorCode.ALREADY_EXIST_SORT);
        faqRepository.save(Faq.builder().title(request.getTitle()).contents(request.getContents()).sort(request.getSort()).member(member).build());
        return FaqSaveDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public FaqModifyDto.Response modifyFaq(FaqModifyDto.Request request, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        Faq faq = faqRepository.findById(request.getId()).orElseThrow(() -> new BusinessException(FaqErrorCode.FAQ_NOT_FOUND));
        faq.modifyFaq(request, member);
        return FaqModifyDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public FaqDeleteDto.Response deleteFaq(Long id, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        Faq faq = faqRepository.findById(id).orElseThrow(() -> new BusinessException(FaqErrorCode.FAQ_NOT_FOUND));
        faq.deleteFaq(member);
        return FaqDeleteDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    public FaqListDto.Response findFaqList(Pageable pageable) {
        return FaqListDto.Response.builder().faqInfoList(faqRepository.findFaqList(pageable)).build();
    }

    public FaqAllListDto.Response findFaqAllList(Pageable pageable) {
        return FaqAllListDto.Response.builder().faqInfoList(faqRepository.findFaqAllList(pageable)).build();
    }

    public FaqDto.Response findFaq(Long id) {
        Faq faq = faqRepository.findById(id).orElseThrow(() -> new BusinessException(FaqErrorCode.FAQ_NOT_FOUND));
        return FaqDto.Response.builder().id(faq.getId()).title(faq.getTitle()).contents(faq.getContents())
                .sort(faq.getSort()).createAt(faq.getCreatedAt()).updatedAt(faq.getUpdatedAt()).build();
    }
}
