package org.example.knockin.inquiry.repository;

import org.example.knockin.inquiry.dto.BoInquiryDetailDto;
import org.example.knockin.inquiry.dto.BoInquiryListDto;
import org.example.knockin.inquiry.dto.InquiryDetailDto;
import org.example.knockin.inquiry.dto.InquiryListDto;
import org.example.knockin.member.entity.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InquiryRepositoryCustom {
    List<InquiryListDto.Response.InquiryItem> findMyInquiryList(Boolean isDeleted, Member member, Pageable pageable);
    InquiryDetailDto.Response.InquiryDetail findMyInquiry(Boolean isDeleted, Member member, Long inquiryId);
    List<BoInquiryListDto.Response.InquiryItem> findBackOfficeInquirieList(Pageable pageable, BoInquiryListDto.Request request);
    BoInquiryDetailDto.Response.InquiryDetail findBackOfficeInquirie(Long id);
    List<BoInquiryDetailDto.Response.InquiryDetail.Reply> findBackOfficeInquirieReply(Long id);
}