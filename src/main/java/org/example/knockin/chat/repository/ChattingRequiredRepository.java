package org.example.knockin.chat.repository;

import org.example.knockin.chat.entity.ChattingRequired;
import org.example.knockin.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRequiredRepository extends JpaRepository<ChattingRequired, Long>, ChattingRequiredRepositoryCustom {
    Page<ChattingRequired> findByRequestee(Member requestee, Pageable pageable);
}
