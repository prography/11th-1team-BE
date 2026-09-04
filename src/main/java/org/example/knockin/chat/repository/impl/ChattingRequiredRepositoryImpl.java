package org.example.knockin.chat.repository.impl;

import static org.example.knockin.member.entity.QBasicInformation.basicInformation;
import static org.example.knockin.chat.entity.QChattingRequired.chattingRequired;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.knockin.chat.entity.ChattingRequired;
import org.example.knockin.chat.entity.ChattingRequiredStatus;
import org.example.knockin.member.entity.Member;
import org.example.knockin.chat.repository.ChattingRequiredRepositoryCustom;
import org.example.knockin.chat.repository.row.ChatRequestListRow;
import org.example.knockin.member.entity.QBasicInformation;
import org.example.knockin.member.entity.QMember;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChattingRequiredRepositoryImpl implements ChattingRequiredRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsBetweenMembers(Member memberA, Member memberB) {
        Integer result = jpaQueryFactory
                .selectOne()
                .from(chattingRequired)
                .where(
                        chattingRequired.requester.eq(memberA).and(chattingRequired.requestee.eq(memberB))
                                .or(chattingRequired.requester.eq(memberB).and(chattingRequired.requestee.eq(memberA)))
                )
                .fetchFirst();

        return result != null;
    }

    @Override
    public Optional<ChattingRequired> findLatest(Member memberA, Member memberB) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(chattingRequired)
                .where(
                        chattingRequired.requester.eq(memberA).and(chattingRequired.requestee.eq(memberB))
                                .or(chattingRequired.requester.eq(memberB).and(chattingRequired.requestee.eq(memberA)))
                )
                .orderBy(chattingRequired.id.desc())
                .fetchFirst());
    }

    @Override
    public List<ChatRequestListRow> findAllPendingByRequestee(Member requestee) {
        QMember requester = new QMember("requester");
        QBasicInformation basicInformationSub = new QBasicInformation("basicInformationSub");

        return jpaQueryFactory
                .select(Projections.constructor(
                        ChatRequestListRow.class,
                        chattingRequired.id,
                        chattingRequired.status,
                        requester.id,
                        basicInformation.name,
                        basicInformation.birth,
                        basicInformation.gender,
                        chattingRequired.createdAt
                ))
                .from(chattingRequired)
                .join(chattingRequired.requester, requester)
                .leftJoin(basicInformation)
                .on(basicInformation.id.eq(
                        JPAExpressions
                                .select(basicInformationSub.id.max())
                                .from(basicInformationSub)
                                .where(basicInformationSub.member.eq(requester))
                ))
                .where(
                        chattingRequired.requestee.eq(requestee),
                        chattingRequired.status.eq(ChattingRequiredStatus.PENDING)
                )
                .orderBy(chattingRequired.createdAt.desc(), chattingRequired.id.desc())
                .fetch();
    }
}
