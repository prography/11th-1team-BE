package org.example.knockin.matching.service;

import java.util.List;
import java.util.Map;
import org.example.knockin.matching.dto.Compatibility;
import org.example.knockin.chat.entity.ChattingRequired;
import org.example.knockin.chat.entity.ChattingScore;

public interface RoommateScoreService {
    Map<Long, Compatibility> calculateScores(Long requesterId, List<Long> targetMemberIds);

    Map<Long, Integer> calculateSimpleScores(Long requesterId, List<Long> targetMemberIds);

    Compatibility calculateScore(Long requesterId, Long targetMemberId);

    Integer calculateSimpleScore(Long requesterId, Long targetMemberId);

    List<ChattingScore> createChattingScores(ChattingRequired chattingRequired);
}
