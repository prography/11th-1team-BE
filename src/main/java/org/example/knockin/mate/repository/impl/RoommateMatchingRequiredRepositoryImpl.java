package org.example.knockin.mate.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import static org.example.knockin.mate.entity.QRoommateMatchingRequired.roommateMatchingRequired;

import org.example.knockin.mate.dto.RoommateRequestDto.RoommateMatchingRequiredInfo;
import org.example.knockin.chat.entity.ChattingRoom;
import org.example.knockin.mate.entity.RoommateMatchingRequired;
import org.example.knockin.mate.repository.RoommateMatchingRequiredRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoommateMatchingRequiredRepositoryImpl implements RoommateMatchingRequiredRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<RoommateMatchingRequired> findLatest(Long chattingRoomId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(roommateMatchingRequired)
                        .from(roommateMatchingRequired)
                        .where(
                                roommateMatchingRequired.chattingRoom.id.eq(chattingRoomId)
                        )
                        .orderBy(roommateMatchingRequired.id.desc())
                        .fetchFirst()
        );
    }

    @Override
    public List<RoommateMatchingRequiredInfo> findRequiredDto(ChattingRoom chattingRoomEntity) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        RoommateMatchingRequiredInfo.class,
                        roommateMatchingRequired.id,
                        roommateMatchingRequired.requester.id,
                        roommateMatchingRequired.requestee.id,
                        roommateMatchingRequired.status,
                        roommateMatchingRequired.createdAt,
                        roommateMatchingRequired.updatedAt
                ))
                .from(roommateMatchingRequired)
                .where(roommateMatchingRequired.chattingRoom.eq(chattingRoomEntity))
                .fetch();
    }

    @Override
    public Page<RoommateMatchingRequired> findMyRequiredList(Long memberId, Pageable pageable) {
        List<RoommateMatchingRequired> content = jpaQueryFactory
                .select(roommateMatchingRequired)
                .from(roommateMatchingRequired)
                .where(roommateMatchingRequired.requester.id.eq(memberId)
                        .or(roommateMatchingRequired.requestee.id.eq(memberId)))
                .orderBy(roommateMatchingRequired.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Long count = jpaQueryFactory
                .select(roommateMatchingRequired.count())
                .from(roommateMatchingRequired)
                .where(roommateMatchingRequired.requester.id.eq(memberId)
                        .or(roommateMatchingRequired.requestee.id.eq(memberId)))
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }
}
