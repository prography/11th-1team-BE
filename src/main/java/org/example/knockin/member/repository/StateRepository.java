package org.example.knockin.member.repository;

import org.example.knockin.member.entity.Member;
import org.example.knockin.member.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Long> {
    List<State> findByMember(Member member);

    List<State> findByMemberId(Long memberId);
}