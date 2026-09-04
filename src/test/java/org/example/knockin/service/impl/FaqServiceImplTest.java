package org.example.knockin.service.impl;

import org.example.knockin.member.service.impl.MemberServiceImpl;
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
import org.example.knockin.meta.service.impl.FaqServiceImpl;
import org.example.knockin.meta.repository.FaqRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("자주 찾는 질문(FAQ) 서비스 테스트")
class FaqServiceImplTest {

    @Mock
    private FaqRepository faqRepository;

    @Mock
    private MemberServiceImpl memberService;

    @InjectMocks
    private FaqServiceImpl faqService;

    @Test
    @DisplayName("FAQ 등록 성공 테스트")
    void saveFaqSuccessTest() {
        // given
        Long memberId = 1L;
        FaqSaveDto.Request request = new FaqSaveDto.Request();
        request.setTitle("FAQ Title");
        request.setContents("FAQ Contents");
        request.setSort(1);

        Member member = mock(Member.class);
        given(memberService.findById(memberId)).willReturn(Optional.of(member));
        given(faqRepository.findBySort(1)).willReturn(Collections.emptyList());

        // when
        FaqSaveDto.Response response = faqService.saveFaq(request, memberId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
        verify(faqRepository).save(any(Faq.class));
    }

    @Test
    @DisplayName("FAQ 등록 시 회원이 없으면 BusinessException 발생")
    void saveFaqMemberNotFoundTest() {
        // given
        Long memberId = 1L;
        FaqSaveDto.Request request = new FaqSaveDto.Request();
        given(memberService.findById(memberId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> faqService.saveFaq(request, memberId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", AuthErrorCode.MEMBER_NOT_FOUND);

        verifyNoInteractions(faqRepository);
    }

    @Test
    @DisplayName("FAQ 등록 시 정렬 순서가 중복되면 BusinessException 발생")
    void saveFaqSortDuplicatedTest() {
        // given
        Long memberId = 1L;
        FaqSaveDto.Request request = new FaqSaveDto.Request();
        request.setSort(1);

        Member member = mock(Member.class);
        Faq existingFaq = mock(Faq.class);

        given(memberService.findById(memberId)).willReturn(Optional.of(member));
        given(faqRepository.findBySort(1)).willReturn(List.of(existingFaq));

        // when & then
        assertThatThrownBy(() -> faqService.saveFaq(request, memberId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", FaqErrorCode.ALREADY_EXIST_SORT);

        verify(faqRepository, never()).save(any(Faq.class));
    }

    @Test
    @DisplayName("FAQ 수정 성공 테스트")
    void modifyFaqSuccessTest() {
        // given
        Long memberId = 1L;
        FaqModifyDto.Request request = new FaqModifyDto.Request();
        request.setId(100L);
        request.setTitle("Modified Title");
        request.setContents("Modified Contents");
        request.setSort(2);

        Member member = mock(Member.class);
        Faq faq = spy(Faq.builder().id(100L).title("Old Title").contents("Old Contents").sort(1).build());

        given(memberService.findById(memberId)).willReturn(Optional.of(member));
        given(faqRepository.findById(100L)).willReturn(Optional.of(faq));

        // when
        FaqModifyDto.Response response = faqService.modifyFaq(request, memberId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
        verify(faq).modifyFaq(request, member);
    }

    @Test
    @DisplayName("FAQ 수정 시 FAQ 대상을 찾지 못하면 BusinessException 발생")
    void modifyFaqNotFoundTest() {
        // given
        Long memberId = 1L;
        FaqModifyDto.Request request = new FaqModifyDto.Request();
        request.setId(100L);

        Member member = mock(Member.class);
        given(memberService.findById(memberId)).willReturn(Optional.of(member));
        given(faqRepository.findById(100L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> faqService.modifyFaq(request, memberId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", FaqErrorCode.FAQ_NOT_FOUND);
    }

    @Test
    @DisplayName("FAQ 삭제 성공 테스트")
    void deleteFaqSuccessTest() {
        // given
        Long memberId = 1L;
        Long faqId = 100L;

        Member member = mock(Member.class);
        Faq faq = spy(Faq.builder().id(faqId).isDeleted(false).build());

        given(memberService.findById(memberId)).willReturn(Optional.of(member));
        given(faqRepository.findById(faqId)).willReturn(Optional.of(faq));

        // when
        FaqDeleteDto.Response response = faqService.deleteFaq(faqId, memberId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
        verify(faq).deleteFaq(member);
    }

    @Test
    @DisplayName("FAQ 목록 조회 성공 테스트")
    void findFaqListSuccessTest() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        FaqListDto.Response.FaqInfo info = new FaqListDto.Response.FaqInfo();
        info.setId(100L);
        info.setTitle("Title");
        info.setSort(1);

        given(faqRepository.findFaqList(pageable)).willReturn(List.of(info));

        // when
        FaqListDto.Response response = faqService.findFaqList(pageable);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getFaqInfoList()).hasSize(1);
        assertThat(response.getFaqInfoList().get(0).getId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("FAQ 전체 상세 목록 조회 성공 테스트")
    void findFaqAllListSuccessTest() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        FaqAllListDto.Response.FaqInfo info = new FaqAllListDto.Response.FaqInfo();
        info.setId(100L);
        info.setTitle("Title");
        info.setContents("Contents");
        info.setSort(1);

        given(faqRepository.findFaqAllList(pageable)).willReturn(List.of(info));

        // when
        FaqAllListDto.Response response = faqService.findFaqAllList(pageable);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getFaqInfoList()).hasSize(1);
        assertThat(response.getFaqInfoList().get(0).getId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("FAQ 단건 조회 성공 테스트")
    void findFaqSuccessTest() {
        // given
        Long faqId = 100L;
        Faq faq = Faq.builder()
                .id(faqId)
                .title("Title")
                .contents("Contents")
                .sort(1)
                .build();
        ReflectionTestUtils.setField(faq, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(faq, "updatedAt", LocalDateTime.now().plusHours(1));

        given(faqRepository.findById(faqId)).willReturn(Optional.of(faq));

        // when
        FaqDto.Response response = faqService.findFaq(faqId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(faqId);
        assertThat(response.getTitle()).isEqualTo("Title");
        assertThat(response.getContents()).isEqualTo("Contents");
        assertThat(response.getSort()).isEqualTo(1);
        assertThat(response.getCreateAt()).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("FAQ 단건 조회 시 대상을 찾지 못하면 BusinessException 발생")
    void findFaqNotFoundTest() {
        // given
        Long faqId = 100L;
        given(faqRepository.findById(faqId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> faqService.findFaq(faqId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", FaqErrorCode.FAQ_NOT_FOUND);
    }
}
