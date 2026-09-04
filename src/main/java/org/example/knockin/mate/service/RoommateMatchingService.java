package org.example.knockin.mate.service;

import org.example.knockin.util.dto.MatchDetailDto;
import org.example.knockin.util.dto.MatchDto;
import org.example.knockin.util.dto.MatchListDto;
import org.example.knockin.util.dto.MatchListDto.Response;
import org.example.knockin.declaration.dto.MemberReportDto;
import org.springframework.data.domain.Slice;

public interface RoommateMatchingService {
    Slice<Response> findMatchingList(Long memberId, MatchListDto.Request request);
    MatchDetailDto.Response findMatchingDetail(Long targetMemberId, Long requesterId);

    MatchDto.Response likeMatching(Long senderId, Long receiverId);

    MemberReportDto.Response reportMatching(Long reporterId, Long reportedId, MemberReportDto.Request request);
}
