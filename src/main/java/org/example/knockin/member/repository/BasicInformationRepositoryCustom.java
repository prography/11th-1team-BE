package org.example.knockin.member.repository;

import java.util.List;
import java.util.Optional;
import org.example.knockin.member.entity.BasicInformation;
import org.example.knockin.member.entity.Member;
import org.example.knockin.member.repository.row.ChattingRoomBasicInfoRow;

public interface BasicInformationRepositoryCustom {
    boolean isExsitBasicInformation(Member member);

    Optional<BasicInformation> findLatestBasicInformation(Member member);

    Optional<ChattingRoomBasicInfoRow> findChattingRoomBasicInfoRow(Long memberId);

    List<ChattingRoomBasicInfoRow> findChattingRoomBasicInfoRows(List<Long> memberIds);
}
