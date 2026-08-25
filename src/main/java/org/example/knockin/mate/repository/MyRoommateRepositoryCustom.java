package org.example.knockin.mate.repository;

import java.util.Optional;
import org.example.knockin.member.entity.Member;
import org.example.knockin.mate.entity.MyRoommate;

public interface MyRoommateRepositoryCustom {
    boolean isExistRoomMate(Member member);

    Optional<MyRoommate> findWithRequiredByMemberId(Long memberId);

    Optional<MyRoommate> findWithRequiredAndMembersByMemberId(Long memberId);
}