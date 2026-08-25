package org.example.knockin.life.repository;


import java.util.List;
import org.example.knockin.board.dto.BoardDetailDto;
import org.example.knockin.life.repository.row.MatchingPreferenceConditionRow;

public interface PreferenceConditionRepositoryCustom {
    List<BoardDetailDto.Response.Condition> getConditionDtoByMemberId(Long memberId);

    List<MatchingPreferenceConditionRow> findAllPreferenceConditionByMemberIdIn(List<Long> memberIds);

    List<Long> findLifeInformationIdByMemberId(Long memberId);
}
