package org.example.knockin.life.repository;

import java.util.List;
import org.example.knockin.board.dto.BoardDetailDto.Response.ConditionWeight;
import org.example.knockin.life.repository.row.MatchingPreferenceConditionWeightRow;

public interface PreferenceConditionWeightRepositoryCustom {
    List<ConditionWeight> getConditionWeightDtoByMemberId(Long memberId);

    List<MatchingPreferenceConditionWeightRow> findAllPreferenceConditionWeightByMemberIdIn(List<Long> memberIds);
}
