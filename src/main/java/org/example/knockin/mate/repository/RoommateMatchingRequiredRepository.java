package org.example.knockin.mate.repository;

import java.util.List;
import org.example.knockin.member.entity.Member;
import org.example.knockin.mate.entity.RoommateMatchingRequired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoommateMatchingRequiredRepository extends JpaRepository<RoommateMatchingRequired, Long>, RoommateMatchingRequiredRepositoryCustom {
    List<RoommateMatchingRequired> findByRequesterAndRequestee(Member requester, Member requestee);

    Page<RoommateMatchingRequired> findByRequesterAndRequestee(Member requester, Member requestee, Pageable pageable);

    Page<RoommateMatchingRequired> findByRequesterIdAndRequesteeId(Long requesterId, Long requesteeId, Pageable pageable);
}