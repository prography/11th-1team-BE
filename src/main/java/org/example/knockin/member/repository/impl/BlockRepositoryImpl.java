package org.example.knockin.member.repository.impl;

import static org.example.knockin.member.entity.QBasicInformation.basicInformation;
import static org.example.knockin.member.entity.QBlock.block;
import static org.example.knockin.member.entity.QMember.member;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.knockin.member.dto.BlockListDto;
import org.example.knockin.member.dto.BlockListDto.Response.Block;
import org.example.knockin.member.repository.BlockRepositoryCustom;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BlockRepositoryImpl implements BlockRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BlockListDto.Response.Block> findMyList(Long blockerId) {

        return jpaQueryFactory
                .select(Projections.fields(
                        Block.class,
                        block.blocked.id.as("userId"),
                        basicInformation.name.as("name"),
                        block.createdAt.as("createAt")
                ))
                .from(block)
                .join(block.blocked, member)
                .leftJoin(basicInformation)
                .on(basicInformation.id.eq(
                        JPAExpressions
                                .select(basicInformation.id.max())
                                .from(basicInformation)
                                .where(basicInformation.member.id.eq(member.id))
                ))
                .where(
                        block.blocker.id.eq(blockerId),
                        block.isDeleted.isFalse()
                )
                .orderBy(block.createdAt.desc(), block.id.desc())
                .fetch();
    }

    @Override
    public boolean existsActiveBlockBetweenMembers(Long firstMemberId, Long secondMemberId) {
        Integer result = jpaQueryFactory
                .selectOne()
                .from(block)
                .where(
                        block.isDeleted.isFalse(),
                        block.blocker.id.eq(firstMemberId)
                                .and(block.blocked.id.eq(secondMemberId))
                                .or(block.blocker.id.eq(secondMemberId)
                                        .and(block.blocked.id.eq(firstMemberId)))
                )
                .fetchFirst();

        return result != null;
    }
}
