package org.example.knockin.mate.service.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.knockin.matching.entity.RoommateScore;
import org.example.knockin.matching.repository.RoommateScoreRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyRoommateScoreServiceImpl {

    private final RoommateScoreRepository roommateScoreRepository;

    public List<RoommateScore> saveAll(List<RoommateScore> roommateScores) {
        return roommateScoreRepository.saveAll(roommateScores);
    }

    public Optional<RoommateScore> findByRoommateIdAndMemberId(Long myRoommateId, Long memberId) {
        return roommateScoreRepository.findOneByMyRoommateIdAndMemberId(myRoommateId, memberId);
    }
}
