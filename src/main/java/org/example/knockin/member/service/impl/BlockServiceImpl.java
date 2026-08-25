package org.example.knockin.member.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.knockin.member.dto.BlockDto;
import org.example.knockin.member.dto.BlockListDto;
import org.example.knockin.member.dto.BlockListDto.Response;
import org.example.knockin.member.entity.Block;
import org.example.knockin.member.entity.Member;
import org.example.knockin.global.exception.BlockErrorCode;
import org.example.knockin.global.exception.BusinessException;
import org.example.knockin.member.repository.BlockRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlockServiceImpl {

    private final MemberServiceImpl memberService;
    private final BlockRepository blockRepository;

    @Transactional
    public BlockDto.Response saveBlock(Long blockerId, Long blockedId) {
        Member blocker = memberService.findByIdOrThrow(blockerId);
        Member blocked = memberService.findByIdOrThrow(blockedId);

        if (blockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId)) {
            throw new BusinessException(BlockErrorCode.DUPLICATE);
        }

        blockRepository.save(Block.builder().blocker(blocker).blocked(blocked).build());
        return BlockDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional(readOnly = true)
    public BlockListDto.Response findMyList(Long memberId, Pageable pageable) {
        Member blocker = memberService.findByIdOrThrow(memberId);
        // TODO: 차단 목록 API의 페이지 응답 계약이 확정되면 pageable을 조회 쿼리에 적용한다.
        List<Response.Block> myList = blockRepository.findMyList(blocker.getId());
        return BlockListDto.Response.builder().blocks(myList).build();
    }

    @Transactional(readOnly = true)
    public boolean isBlockedBetween(Long firstMemberId, Long secondMemberId) {
        return blockRepository.existsActiveBlockBetweenMembers(firstMemberId, secondMemberId);
    }

    @Transactional
    public BlockDto.Response deleteBlock(Long blockerId, Long blockedId) {
        Member blocker = memberService.findByIdOrThrow(blockerId);
        Member blocked = memberService.findByIdOrThrow(blockedId);

        Block block = blockRepository.findOneByBlockerAndBlocked(blocker, blocked)
                .orElseThrow(() -> new BusinessException(BlockErrorCode.NOT_FOUND));

        blockRepository.delete(block);

        return BlockDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }
}
