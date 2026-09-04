package org.example.knockin.board.repository;

import java.util.List;
import java.util.Optional;
import org.example.knockin.board.entity.RoommateBoard;
import org.example.knockin.board.entity.RoommateBoardInterest;
import org.example.knockin.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoommateBoardInterestRepository extends JpaRepository<RoommateBoardInterest, Long>, RoommateBoardInterestRepositoryCustom {
    Optional<RoommateBoardInterest> findByRoommateBoardAndMember(RoommateBoard roommateBoard, Member member);

    List<RoommateBoardInterest> findAllByRoommateBoardIdAndMemberId(Long roommateBoardId, Long memberId);

    boolean existsByRoommateBoardIdAndMemberIdAndIsDeletedIsFalse(Long roommateBoardId, Long memberId);
}
