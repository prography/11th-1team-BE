package org.example.knockin.chat.repository;

import java.util.List;
import java.util.Optional;
import org.example.knockin.chat.entity.ChatRoomMember;
import org.example.knockin.chat.entity.ChattingRoom;
import org.example.knockin.member.entity.Member;

public interface ChatRoomMemberRepositoryCustom {
    boolean existsActiveMember(Long chatRoomId, Long memberId);

    Optional<ChatRoomMember> findActiveMemberByRoomIdAndMemberId(Long chatRoomId, Long memberId);

    Member findPartnerMember(ChatRoomMember me, ChattingRoom chattingRoom);

    Member findPartnerMember(ChatRoomMember me, Long chattingRoomId);

    List<ChatRoomMember> findChatRoomMemberById(Long chatRoomId);
}
