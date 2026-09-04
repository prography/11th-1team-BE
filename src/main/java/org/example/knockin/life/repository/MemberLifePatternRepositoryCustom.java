package org.example.knockin.life.repository;

import java.util.List;
import org.example.knockin.board.dto.BoardDetailDto;
import org.example.knockin.member.entity.Member;
import org.example.knockin.life.repository.row.MatchingLifestyleRow;

public interface MemberLifePatternRepositoryCustom {
    List<BoardDetailDto.Response.Lifestyle> getLifeStyleDto(Long memberId);
    boolean isExsitLifeStyle(Member member);
    List<MatchingLifestyleRow> findAllLifestyleByMemberIdIn(List<Long> memberIds);
}