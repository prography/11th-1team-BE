package org.example.knockin.declaration.repository;

import org.example.knockin.member.entity.Member;
import org.example.knockin.declaration.entity.MemberDeclaration;
import org.example.knockin.member.repository.MemberDeclarationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberDeclarationRepository extends JpaRepository<MemberDeclaration, Long>,
        MemberDeclarationRepositoryCustom {
    boolean existsByReporterAndReported(Member reporter, Member reported);
}
