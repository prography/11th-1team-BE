package org.example.knockin.chat.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.knockin.chat.entity.ChattingScore;
import org.example.knockin.chat.repository.ChattingScoreRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingScoreServiceImpl {

    private final ChattingScoreRepository chattingScoreRepository;

    public List<ChattingScore> saveAll(List<ChattingScore> chattingScores) {
        return chattingScoreRepository.saveAll(chattingScores);
    }

    public Optional<ChattingScore> findByChattingRequiredIdAndMemberId(Long chattingRequiredId, Long memberId) {
        return chattingScoreRepository.findOneByChattingRequiredIdAndMemberId(chattingRequiredId, memberId);
    }

    public List<ChattingScore> findByChattingRequiredId(Long chattingRequiredId) {
        return chattingScoreRepository.findByChattingRequiredId(chattingRequiredId);
    }
}
