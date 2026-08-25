package org.example.knockin.room.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.knockin.member.entity.Member;
import org.example.knockin.room.repository.RoomProfileRepositoryCustom;
import org.springframework.stereotype.Repository;

import static org.example.knockin.room.entity.QRoomProfile.roomProfile;

@Repository
@RequiredArgsConstructor
public class RoomProfileRepositoryImpl implements RoomProfileRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean isExsitRoomProfile(Member member) {
        Long result = jpaQueryFactory.select(roomProfile.id).from(roomProfile).where(roomProfile.member.eq(member)).fetchFirst();
        return result != null;
    }
}