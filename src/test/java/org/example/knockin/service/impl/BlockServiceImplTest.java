package org.example.knockin.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.example.knockin.member.dto.BlockDto;
import org.example.knockin.member.dto.BlockListDto;
import org.example.knockin.member.entity.Block;
import org.example.knockin.member.entity.Member;
import org.example.knockin.global.exception.BlockErrorCode;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.member.repository.BlockRepository;
import org.example.knockin.member.service.impl.BlockServiceImpl;
import org.example.knockin.member.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@DisplayName("차단 서비스 테스트")
class BlockServiceImplTest {

    @Mock
    private MemberServiceImpl memberService;

    @Mock
    private BlockRepository blockRepository;

    @InjectMocks
    private BlockServiceImpl blockService;

    @Test
    @DisplayName("차단 이력이 없으면 차단 정보를 저장한다")
    void saveBlockSuccess() {
        // given
        Member blocker = Member.builder().id(1L).build();
        Member blocked = Member.builder().id(2L).build();
        given(memberService.findByIdOrThrow(1L)).willReturn(blocker);
        given(memberService.findByIdOrThrow(2L)).willReturn(blocked);
        given(blockRepository.existsByBlockerIdAndBlockedId(1L, 2L)).willReturn(false);
        ArgumentCaptor<Block> blockCaptor = ArgumentCaptor.forClass(Block.class);

        // when
        BlockDto.Response response = blockService.saveBlock(1L, 2L);

        // then
        assertThat(response.getUpdatedAt()).isNotNull();
        verify(blockRepository).save(blockCaptor.capture());
        assertThat(blockCaptor.getValue().getBlocker()).isSameAs(blocker);
        assertThat(blockCaptor.getValue().getBlocked()).isSameAs(blocked);
    }

    @Test
    @DisplayName("이미 차단한 회원은 다시 차단할 수 없다")
    void saveBlockFailsWhenDuplicate() {
        // given
        Member blocker = Member.builder().id(1L).build();
        Member blocked = Member.builder().id(2L).build();
        given(memberService.findByIdOrThrow(1L)).willReturn(blocker);
        given(memberService.findByIdOrThrow(2L)).willReturn(blocked);
        given(blockRepository.existsByBlockerIdAndBlockedId(1L, 2L)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> blockService.saveBlock(1L, 2L))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", BlockErrorCode.DUPLICATE);
        verify(blockRepository, never()).save(any(Block.class));
    }

    @Test
    @DisplayName("내 차단 목록에서 차단한 회원의 이름과 차단 일시를 조회한다")
    void findMyListSuccess() {
        // given
        Member blocker = Member.builder().id(1L).build();
        LocalDateTime blockedAt = LocalDateTime.of(2026, 7, 25, 12, 0);
        BlockListDto.Response.Block block = BlockListDto.Response.Block.builder()
                .userId(2L)
                .name("차단회원")
                .createAt(blockedAt)
                .build();
        Pageable pageable = PageRequest.of(0, 20);

        given(memberService.findByIdOrThrow(1L)).willReturn(blocker);
        given(blockRepository.findMyList(1L)).willReturn(List.of(block));

        // when
        BlockListDto.Response response = blockService.findMyList(1L, pageable);

        // then
        assertThat(response.getBlocks()).hasSize(1);
        assertThat(response.getBlocks().getFirst().getUserId()).isEqualTo(2L);
        assertThat(response.getBlocks().getFirst().getName()).isEqualTo("차단회원");
        assertThat(response.getBlocks().getFirst().getCreateAt()).isEqualTo(blockedAt);
        verify(blockRepository).findMyList(1L);
    }

    @Test
    @DisplayName("두 회원의 양방향 활성 차단 여부를 조회한다")
    void isBlockedBetweenDelegatesToRepository() {
        // given
        given(blockRepository.existsActiveBlockBetweenMembers(1L, 2L)).willReturn(true);

        // when
        boolean blocked = blockService.isBlockedBetween(1L, 2L);

        // then
        assertThat(blocked).isTrue();
        verify(blockRepository).existsActiveBlockBetweenMembers(1L, 2L);
    }

    @Test
    @DisplayName("차단 이력이 있으면 차단을 해제한다")
    void deleteBlockSuccess() {
        // given
        Member blocker = Member.builder().id(1L).build();
        Member blocked = Member.builder().id(2L).build();
        Block block = Block.builder().blocker(blocker).blocked(blocked).build();
        given(memberService.findByIdOrThrow(1L)).willReturn(blocker);
        given(memberService.findByIdOrThrow(2L)).willReturn(blocked);
        given(blockRepository.findOneByBlockerAndBlocked(blocker, blocked))
                .willReturn(Optional.of(block));

        // when
        BlockDto.Response response = blockService.deleteBlock(1L, 2L);

        // then
        assertThat(response.getUpdatedAt()).isNotNull();
        verify(blockRepository).delete(block);
    }

    @Test
    @DisplayName("차단 이력이 없으면 차단을 해제할 수 없다")
    void deleteBlockFailsWhenNotFound() {
        // given
        Member blocker = Member.builder().id(1L).build();
        Member blocked = Member.builder().id(2L).build();
        given(memberService.findByIdOrThrow(1L)).willReturn(blocker);
        given(memberService.findByIdOrThrow(2L)).willReturn(blocked);
        given(blockRepository.findOneByBlockerAndBlocked(blocker, blocked))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> blockService.deleteBlock(1L, 2L))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", BlockErrorCode.NOT_FOUND);
        verify(blockRepository, never()).delete(any(Block.class));
    }
}
