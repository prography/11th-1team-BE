package org.example.knockin.chat.repository;

import java.util.List;
import java.util.Optional;
import org.example.knockin.chat.entity.ChattingRequired;
import org.example.knockin.member.entity.Member;
import org.example.knockin.chat.repository.row.ChatRequestListRow;

public interface ChattingRequiredRepositoryCustom {
    boolean existsBetweenMembers(Member memberA, Member memberB);

    Optional<ChattingRequired> findLatest(Member memberA, Member memberB);

    List<ChatRequestListRow> findAllPendingByRequestee(Member requestee);
}
