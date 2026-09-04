package org.example.knockin.declaration.repository;

import java.util.Optional;
import org.example.knockin.board.entity.RoommateBoard;
import org.example.knockin.declaration.entity.RoommateBoardDeclaration;
import org.example.knockin.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoommateBoardDeclarationRepository extends JpaRepository<RoommateBoardDeclaration, Long>, RoommateBoardDeclarationRepositoryCustom {
    Optional<RoommateBoardDeclaration> findByRoommateBoardAndMember(RoommateBoard roommateBoard, Member member);
    Optional<RoommateBoardDeclaration> findById(Long id);
}