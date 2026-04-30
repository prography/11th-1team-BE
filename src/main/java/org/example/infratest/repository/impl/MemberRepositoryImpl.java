package org.example.infratest.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.infratest.entity.MemberEntity;
import org.example.infratest.repository.MemberRepositoryCustom;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.example.infratest.entity.QMemberEntity.memberEntity;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MemberEntity> searchMembers(String username) {
        return jpaQueryFactory
                .selectFrom(memberEntity)
                .where(nameEq(username))
                .fetch();
    }

    private BooleanExpression nameEq(String username) {
        return StringUtils.hasText(username) ? memberEntity.name.eq(username) : null;
    }
}
