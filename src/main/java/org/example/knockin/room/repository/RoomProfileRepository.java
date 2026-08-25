package org.example.knockin.room.repository;

import org.example.knockin.member.entity.Member;
import org.example.knockin.room.entity.RoomProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomProfileRepository extends JpaRepository<RoomProfile, Long>, RoomProfileRepositoryCustom {
    List<RoomProfile> findByMember(Member member);
}