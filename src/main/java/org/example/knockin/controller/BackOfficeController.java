package org.example.knockin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.knockin.agreement.dto.BoTermsDetailDto;
import org.example.knockin.agreement.dto.BoTermsDto;
import org.example.knockin.agreement.dto.BoTermsListDto;
import org.example.knockin.agreement.dto.BoTypeTermsDto;
import org.example.knockin.agreement.dto.BoTypeTermsListDto;
import org.example.knockin.verification.dto.BoVerificationApproveListDto;
import org.example.knockin.verification.dto.BoVerificationCancelListDto;
import org.example.knockin.verification.dto.BoVerificationDto;
import org.example.knockin.verification.dto.BoVerificationWaitingDetailDto;
import org.example.knockin.verification.dto.BoVerificationWaitingListDto;
import org.example.knockin.board.dto.BoBoardDeleteDto;
import org.example.knockin.board.dto.BoBoardDetailDto;
import org.example.knockin.board.dto.BoBoardListDto;
import org.example.knockin.declaration.dto.BoReportDoneListDto;
import org.example.knockin.declaration.dto.BoReportHiddenDto;
import org.example.knockin.declaration.dto.BoReportNoActionDto;
import org.example.knockin.declaration.dto.BoReportSuspendedDto;
import org.example.knockin.declaration.dto.BoReportWaitListDto;
import org.example.knockin.global.api.CommonResponse;
import org.example.knockin.global.auth.dto.PrincipalDetails;
import org.example.knockin.inquiry.dto.BoInquiryDetailDto;
import org.example.knockin.inquiry.dto.BoInquiryListDto;
import org.example.knockin.inquiry.dto.BoInquiryReplyDto;
import org.example.knockin.life.dto.BoLifeStylePatternDetailDto;
import org.example.knockin.life.dto.BoLifeStylePatternDto;
import org.example.knockin.life.dto.BoLifeStylePatternListDto;
import org.example.knockin.member.dto.BoMemberAuthDto;
import org.example.knockin.member.dto.BoMemberCancelDto;
import org.example.knockin.member.dto.BoMemberDetailDto;
import org.example.knockin.member.dto.BoMemberListDto;
import org.example.knockin.member.dto.BoMemberUnCancelDto;
import org.example.knockin.meta.dto.AppVersionListDto;
import org.example.knockin.meta.dto.AppVersionModifyDto;
import org.example.knockin.meta.dto.AppVersionSaveDto;
import org.example.knockin.meta.dto.AuthEmailDeleteDto;
import org.example.knockin.meta.dto.AuthEmailListDto;
import org.example.knockin.meta.dto.AuthEmailModifyDto;
import org.example.knockin.meta.dto.AuthEmailSaveDto;
import org.example.knockin.meta.dto.BoNoticeDetailDto;
import org.example.knockin.meta.dto.BoNoticeDto;
import org.example.knockin.meta.dto.BoNoticeListDto;
import org.example.knockin.meta.dto.FaqDeleteDto;
import org.example.knockin.meta.dto.FaqDto;
import org.example.knockin.meta.dto.FaqListDto;
import org.example.knockin.meta.dto.FaqModifyDto;
import org.example.knockin.meta.dto.FaqSaveDto;
import org.example.knockin.meta.service.impl.AppVersionServiceImpl;
import org.example.knockin.meta.service.impl.AuthEmailServiceImpl;
import org.example.knockin.meta.service.impl.FaqServiceImpl;
import org.example.knockin.room.dto.BoRoomAddOptionDetailDto;
import org.example.knockin.room.dto.BoRoomAddOptionDto;
import org.example.knockin.room.dto.BoRoomAddOptionListDto;
import org.example.knockin.room.dto.BoRoomTypeDetailDto;
import org.example.knockin.room.dto.BoRoomTypeDto;
import org.example.knockin.room.dto.BoRoomTypeListDto;
import org.example.knockin.service.impl.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bo")
@Tag(name = "10. 백오피스 [BO]")
public class BackOfficeController {
    private final FaqServiceImpl faqService;
    private final AppVersionServiceImpl appVersionService;
    private final AuthEmailServiceImpl authEmailService;
    private final BackOfficeServiceImpl backOfficeService;

    @GetMapping("/type/terms")
    @Operation(summary = "약관 유형 목록 조회")
    public CommonResponse<BoTypeTermsListDto.Response> findTypeTermsList() {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findTypeTermsList());
    }

    @PutMapping("/type/term/{termTypeId}")
    @Operation(summary = "약관 유형 수정")
    public CommonResponse<BoTypeTermsDto.Response> modifyTermType(@PathVariable Long termTypeId, @RequestBody BoTypeTermsDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.modifyTermType(termTypeId, request));
    }

    @PostMapping("/type/terms")
    @Operation(summary = "약관 유형 저장")
    public CommonResponse<BoTypeTermsDto.Response> saveTermType(@Valid @RequestBody BoTypeTermsDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.saveTermType(request));
    }

    @DeleteMapping("/type/term/{termTypeId}")
    @Operation(summary = "약관 유형 삭제")
    public CommonResponse<BoTypeTermsDto.Response> deleteTermType(@PathVariable Long termTypeId) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.deleteTermType(termTypeId));
    }

    @PostMapping("/terms")
    @Operation(summary = "약관 저장")
    public CommonResponse<BoTermsDto.Response> saveTerms(@RequestBody BoTermsDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.saveTerms(request));
    }

    @PutMapping("/terms/{termsId}/draft")
    @Operation(summary = "약관 수정 (임시저장)")
    public CommonResponse<BoTermsDto.Response> modifyTerms(@PathVariable Long termsId, @RequestBody BoTermsDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.modifyTerms(request, termsId));
    }

    @GetMapping("/terms")
    @Operation(summary = "약관 목록 조회")
    public CommonResponse<BoTermsListDto.Response> findTermsList(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, BoTermsListDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findTermsList(pageable, request));
    }

    @GetMapping("/terms/{termsId}")
    @Operation(summary = "약관 상세 조회")
    public CommonResponse<BoTermsDetailDto.Response> findTerms(@PathVariable Long termsId) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findTerms(termsId));
    }

    @DeleteMapping("/terms/{termsId}")
    @Operation(summary = "약관 삭제")
    public CommonResponse<BoTermsDto.Response> deleteTerms(@PathVariable Long termsId) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.deleteTerms(termsId));
    }

    @PutMapping("/terms/{termsId}/publish")
    @Operation(summary = "약관 수정 (게시)")
    public CommonResponse<BoTermsDto.Response> modifyLastTerms(@PathVariable Long termsId, @RequestBody BoTermsDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.modifyLastTerms(request, termsId));
    }

    @PostMapping(value = "/room-add-options", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "방 추가 옵션 저장")
    public CommonResponse<BoRoomAddOptionDto.Response> saveRoomAddOptions(@RequestPart(value = "request") BoRoomAddOptionDto.Request request, @RequestPart(value = "file", required = false) MultipartFile file) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.saveRoomAddOptions(request, file));
    }

    @GetMapping("/room-add-options")
    @Operation(summary = "방 추가 옵션 목록 조회")
    public CommonResponse<BoRoomAddOptionListDto.Response> findRoomAddOptionsList(@PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findRoomAddOptionsList(pageable));
    }

    @PutMapping(value = "/room-add-options/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "방 추가 옵션 수정")
    public CommonResponse<BoRoomAddOptionDto.Response> modifyRoomAddOptions(@PathVariable Long id, @RequestPart(value = "request") BoRoomAddOptionDto.Request request, @RequestPart(value = "file", required = false) MultipartFile file) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.modifyRoomAddOptions(request, id, file));
    }

    @DeleteMapping("/room-add-options/{id}")
    @Operation(summary = "방 추가 옵션 삭제")
    public CommonResponse<BoRoomAddOptionDto.Response> deleteRoomAddOptions(@PathVariable Long id) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.deleteRoomAddOptions(id));
    }

    @GetMapping("/room-add-options/{id}")
    @Operation(summary = "방 추가 옵션 상세 조회")
    public CommonResponse<BoRoomAddOptionDetailDto.Response> findRoomAddOptions(@PathVariable Long id) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findRoomAddOptions(id));
    }

    @PostMapping(value = "/room-types", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "방 유형 저장")
    public CommonResponse<BoRoomTypeDto.Response> saveRoomType(@RequestPart(value = "request") BoRoomTypeDto.Request request, @RequestPart(value = "file", required = false) MultipartFile file) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.saveRoomType(request, file));
    }

    @GetMapping("/room-types")
    @Operation(summary = "방 유형 목록 조회")
    public CommonResponse<BoRoomTypeListDto.Response> findRoomTypeList(@PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findRoomTypeList(pageable));
    }

    @PutMapping(value = "/room-types/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "방 유형 수정")
    public CommonResponse<BoRoomTypeDto.Response> modifyRoomType(@PathVariable Long id, @RequestPart(value = "request") BoRoomTypeDto.Request request, @RequestPart(value = "file", required = false) MultipartFile file) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.modifyRoomType(request, id, file));
    }

    @DeleteMapping("/room-types/{id}")
    @Operation(summary = "방 유형 삭제")
    public CommonResponse<BoRoomTypeDto.Response> deleteRoomType(@PathVariable Long id) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.deleteRoomType(id));
    }

    @GetMapping("/room-types/{id}")
    @Operation(summary = "방 유형 상세 조회")
    public CommonResponse<BoRoomTypeDetailDto.Response> findRoomType(@PathVariable Long id) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findRoomType(id));
    }

    @GetMapping("/lifestyle-patterns")
    @Operation(summary = "라이프스타일 패턴 목록 조회")
    public CommonResponse<BoLifeStylePatternListDto.Response> findLifeStylePatternList(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findLifeStylePatternList(pageable));
    }

    @GetMapping("/lifestyle-patterns/{id}")
    @Operation(summary = "라이프스타일 패턴 상세 조회")
    public CommonResponse<BoLifeStylePatternDetailDto.Response> findLifeStylePattern(@PathVariable Long id) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findLifeStylePattern(id));
    }

    @PostMapping(value = "/lifestyle-patterns", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "라이프스타일 패턴 저장")
    public CommonResponse<BoLifeStylePatternDto.Response> saveLifeStylePattern(@RequestPart(value = "request") BoLifeStylePatternDto.Request request, @RequestPart(value = "file", required = false) MultipartFile file) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.saveLifeStylePattern(request, file));
    }

    @PutMapping(value = "/lifestyle-patterns/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "라이프스타일 패턴 수정")
    public CommonResponse<BoLifeStylePatternDto.Response> modifyLifeStylePattern(@PathVariable Long id, @RequestPart(value = "request") BoLifeStylePatternDto.Request request, @RequestPart(value = "file", required = false) MultipartFile file) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.modifyLifeStylePattern(request, id, file));
    }

    @DeleteMapping("/lifestyle-patterns/{id}")
    @Operation(summary = "라이프스타일 패턴 삭제")
    public CommonResponse<BoLifeStylePatternDto.Response> deleteLifeStylePattern(@PathVariable Long id) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.deleteLifeStylePattern(id));
    }

    @GetMapping("/verifications/approve")
    @Operation(summary = "인증 승인완료 목록 조회")
    public CommonResponse<BoVerificationApproveListDto.Response> findVerificationApproves(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findVerificationApproves(pageable));
    }

    @GetMapping("/verifications/cancel")
    @Operation(summary = "인증 반려 목록 조회")
    public CommonResponse<BoVerificationCancelListDto.Response> findVerificationCancels(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findVerificationCancels(pageable));
    }

    @GetMapping("/verifications/wait")
    @Operation(summary = "인증 목록 조회")
    public CommonResponse<BoVerificationWaitingListDto.Response> findVerificationsList(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findVerificationsList(pageable));
    }

    @GetMapping("/verifications/wait/{id}")
    @Operation(summary = "인증 상세보기")
    public CommonResponse<BoVerificationWaitingDetailDto.Response> findVerifications(@PathVariable Long id) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findVerifications(id));
    }

    @PatchMapping("/verifications/wait/{id}/approve")
    @Operation(summary = "인증 승인")
    public CommonResponse<BoVerificationDto.Response> saveVerifications(@PathVariable Long id) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.saveVerifications(id));
    }

    @PatchMapping("/verifications/wait/{id}/cancel")
    @Operation(summary = "인증 반려")
    public CommonResponse<BoVerificationDto.Response> deleteVerifications(@PathVariable Long id, @RequestBody(required = false) BoVerificationDto.Request request) {
        String rejectReason = request != null ? request.getRejectReason() : null;
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.deleteVerifications(id, rejectReason));
    }

    @PostMapping("/notices")
    @Operation(summary = "공지사항 저장")
    public CommonResponse<BoNoticeDto.Response> saveNotice(@RequestBody BoNoticeDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.saveNotice(request, principalDetails.getMember().getId()));
    }

    @GetMapping("/notices")
    @Operation(summary = "공지사항 목록 조회")
    public CommonResponse<BoNoticeListDto.Response> findNoticeList(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findNoticeList(pageable));
    }

    @GetMapping("/notices/{id}")
    @Operation(summary = "공지사항 상세 조회")
    public CommonResponse<BoNoticeDetailDto.Response> findNotice(@PathVariable Long id) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findNotice(id));
    }

    @PutMapping("/notices/{id}")
    @Operation(summary = "공지사항 수정")
    public CommonResponse<BoNoticeDto.Response> modifyNotice(@PathVariable Long id, @RequestBody BoNoticeDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.modifyNotice(request, id, principalDetails.getMember().getId()));
    }

    @DeleteMapping("/notices/{id}")
    @Operation(summary = "공지사항 삭제")
    public CommonResponse<BoNoticeDto.Response> deleteNotice(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.deleteNotice(id, principalDetails.getMember().getId()));
    }

    @PostMapping("/inquiries")
    @Operation(summary = "문의 답변 저장")
    public CommonResponse<BoInquiryReplyDto.Response> saveInquiryReply(@RequestBody BoInquiryReplyDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.saveInquiryReply(request, principalDetails.getMember().getId()));
    }

    @GetMapping("/inquiries")
    @Operation(summary = "문의 목록 조회")
    public CommonResponse<BoInquiryListDto.Response> findInquirieList(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, BoInquiryListDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findInquirieList(pageable, request));
    }

    @GetMapping("/inquiries/{id}")
    @Operation(summary = "문의 상세 조회")
    public CommonResponse<BoInquiryDetailDto.Response> findInquirie(@PathVariable Long id) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findInquirie(id));
    }

    @PostMapping("/faq")
    @Operation(summary = "자주묻는 질문 저장")
    public CommonResponse<FaqSaveDto.Response> saveFaq(@RequestBody FaqSaveDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(faqService.saveFaq(request, principalDetails.getMember().getId()));
    }

    @PutMapping("/faq")
    @Operation(summary = "자주묻는 질문 수정")
    public CommonResponse<FaqModifyDto.Response> modifyFaq(@RequestBody FaqModifyDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(faqService.modifyFaq(request, principalDetails.getMember().getId()));
    }

    @DeleteMapping("/faq/{id}")
    @Operation(summary = "자주묻는 질문 삭제")
    public CommonResponse<FaqDeleteDto.Response> deleteFaq(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(faqService.deleteFaq(id, principalDetails.getMember().getId()));
    }

    @GetMapping("/faq")
    @Operation(summary = "자주묻는 질문 목록 조회")
    public CommonResponse<FaqListDto.Response> findFaqList(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.status(HttpStatus.OK).body(faqService.findFaqList(pageable));
    }

    @GetMapping("/faq/{id}")
    @Operation(summary = "자주묻는 질문 상세 조회")
    public CommonResponse<FaqDto.Response> findFaq(@PathVariable Long id) {
        return CommonResponse.status(HttpStatus.OK).body(faqService.findFaq(id));
    }

    @GetMapping("/app-version")
    @Operation(summary = "현재 앱버전 전체 조회")
    public CommonResponse<AppVersionListDto.Response> findAppVersion(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.status(HttpStatus.OK).body(appVersionService.findAppVersion(pageable));
    }

    @GetMapping("/app-version/ios")
    @Operation(summary = "현재 앱버전 ios 조회")
    public CommonResponse<AppVersionListDto.Response> findAppIosVersion(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.status(HttpStatus.OK).body(appVersionService.findAppIosVersion(pageable));
    }

    @GetMapping("/app-version/android")
    @Operation(summary = "현재 앱버전 android 조회")
    public CommonResponse<AppVersionListDto.Response> findAppAndroidVersion(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.status(HttpStatus.OK).body(appVersionService.findAppAndroidVersion(pageable));
    }

    @PostMapping("/app-version")
    @Operation(summary = "앱버전 삽입")
    public CommonResponse<AppVersionSaveDto.Response> saveAppVersion(@RequestBody AppVersionSaveDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(appVersionService.saveAppVersion(request));
    }

    @PutMapping("/app-version")
    @Operation(summary = "앱버전 수정")
    public CommonResponse<AppVersionModifyDto.Response> modifyAppVersion(@RequestBody AppVersionModifyDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(appVersionService.modifyAppVersion(request));
    }

    @GetMapping("/auth-email")
    @Operation(summary = "인증 이메일 목록 조회")
    public CommonResponse<AuthEmailListDto.Response> findAuthEmailList() {
        return CommonResponse.status(HttpStatus.OK).body(authEmailService.findAuthEmailList());
    }

    @PostMapping("/auth-email")
    @Operation(summary = "인증 이메일 정보 삽입")
    public CommonResponse<AuthEmailSaveDto.Response> saveAuthEmail(@RequestBody AuthEmailSaveDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(authEmailService.saveAuthEmail(request));
    }

    @PutMapping("/auth-email")
    @Operation(summary = "인증 이메일 정보 수정")
    public CommonResponse<AuthEmailModifyDto.Response> modifyAuthEmail(@RequestBody AuthEmailModifyDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(authEmailService.modifyAuthEmail(request));
    }

    @DeleteMapping("/auth-email/{id}")
    @Operation(summary = "인증 이메일 정보 삭제")
    public CommonResponse<AuthEmailDeleteDto.Response> deleteAuthEmail(@PathVariable Long id) {
        return CommonResponse.status(HttpStatus.OK).body(authEmailService.deleteAuthEmail(id));
    }

    @GetMapping("/member")
    @Operation(summary = "회원 관리 목록 조회")
    public CommonResponse<BoMemberListDto.Response> findMemberList(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, BoMemberListDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findMemberList(pageable, request));
    }

    @GetMapping("/member/{id}")
    @Operation(summary = "회원 관리 상세 조회")
    public CommonResponse<BoMemberDetailDto.Response> findMember(@PathVariable Long id) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findMember(id));
    }

    @PatchMapping("/member/cancel/{id}")
    @Operation(summary = "회원 정지")
    public CommonResponse<BoMemberCancelDto.Response> deleteMember(@PathVariable Long id, @RequestBody BoMemberCancelDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.deleteMember(id, request));
    }

    @PatchMapping("/member/uncancel/{id}")
    @Operation(summary = "회원 정지해제")
    public CommonResponse<BoMemberUnCancelDto.Response> unDeleteMember(@PathVariable Long id) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.unDeleteMember(id));
    }

    @PatchMapping("/member/auth/{id}")
    @Operation(summary = "회원 권한 수정")
    public CommonResponse<BoMemberAuthDto.Response> authMember(@PathVariable Long id, @RequestBody BoMemberAuthDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.authMember(id, request));
    }

    @GetMapping("/report/wait")
    @Operation(summary = "신고 관리 대기 목록 조회")
    public CommonResponse<BoReportWaitListDto.Response> findReportWaitList(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findReportWaitList(pageable));
    }

    @GetMapping("/report/done")
    @Operation(summary = "신고 관리 완료 목록 조회")
    public CommonResponse<BoReportDoneListDto.Response> findReportDoneList(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findReportDoneList(pageable));
    }

    @PostMapping("/report/hidden")
    @Operation(summary = "신고 관리 게시물 비공개")
    public CommonResponse<BoReportHiddenDto.Response> reportHidden(@RequestBody BoReportHiddenDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.reportHidden(request));
    }

    @PostMapping("/report/no-action")
    @Operation(summary = "신고 관리 무혐의")
    public CommonResponse<BoReportNoActionDto.Response> reportNoAction(@RequestBody BoReportNoActionDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.reportNoAction(request));
    }

    @PostMapping("/report/suspended")
    @Operation(summary = "신고 관리 정지")
    public CommonResponse<BoReportSuspendedDto.Response> reportSuspended(@RequestBody BoReportSuspendedDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.reportSuspended(request));
    }

    @GetMapping("/boards")
    @Operation(summary = "게시글 목록 조회")
    public CommonResponse<BoBoardListDto.Response> findBoardList(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, BoBoardListDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findBoardList(pageable, request));
    }

    @GetMapping("/board/{id}")
    @Operation(summary = "게시글 상세 조회")
    public CommonResponse<BoBoardDetailDto.Response> findBoard(@PathVariable Long id) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.findBoard(id));
    }

    @DeleteMapping("/board/{id}")
    @Operation(summary = "게시글 삭제")
    public CommonResponse<BoBoardDeleteDto.Response> deleteBoard(@PathVariable Long id, @RequestBody BoBoardDeleteDto.Request request) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.deleteBoard(id, request));
    }

    @PatchMapping("/board/recover/{id}")
    @Operation(summary = "게시글 삭제 복구")
    public CommonResponse<BoBoardDeleteDto.Response> recoverDeleteBoard(@PathVariable Long id) {
        return CommonResponse.status(HttpStatus.OK).body(backOfficeService.recoverDeleteBoard(id));
    }
}

