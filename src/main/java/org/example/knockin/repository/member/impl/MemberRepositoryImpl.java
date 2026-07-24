package org.example.knockin.repository.member.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.example.knockin.dto.BoMemberDetailDto;
import org.example.knockin.dto.BoMemberListDto;
import org.example.knockin.dto.MyPreferencesAllDto;
import org.example.knockin.dto.MyProfileAllDto;
import org.example.knockin.entity.auth.ApproveType;
import org.example.knockin.entity.auth.LoginProviderType;
import org.example.knockin.entity.member.Member;
import org.example.knockin.entity.member.MemberPrivacyType;
import org.example.knockin.entity.member.MemberState;
import org.example.knockin.entity.member.QBasicInformation;
import org.example.knockin.entity.member.QState;
import org.example.knockin.entity.file.QBasicInformationFile;
import org.example.knockin.entity.room.Region;
import org.example.knockin.entity.room.RoomOfferProfile;
import org.example.knockin.entity.room.RoomProfile;
import org.example.knockin.entity.room.RoomSeekerProfile;
import org.example.knockin.dto.AuthResponse;
import org.example.knockin.repository.member.MemberRepositoryCustom;
import org.example.knockin.repository.member.row.MatchingBasicInfoRow;
import org.example.knockin.repository.member.row.MemberWithNameRow;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.example.knockin.entity.member.QBlock.block;
import static org.example.knockin.entity.member.QMember.member;
import static org.example.knockin.entity.life.QPreferenceCondition.preferenceCondition;
import static org.example.knockin.entity.life.QMemberLifePattern.memberLifePattern;
import static org.example.knockin.entity.life.QPreferenceConditionWeight.preferenceConditionWeight;
import static org.example.knockin.entity.member.QBasicInformation.basicInformation;
import static org.example.knockin.entity.life.QLifePatternInformation.lifePatternInformation;
import static org.example.knockin.entity.life.QLifePattern.lifePattern;
import static org.example.knockin.entity.file.QBasicInformationFile.basicInformationFile;
import static org.example.knockin.entity.file.QFile.file;
import static org.example.knockin.entity.room.QRoomProfile.roomProfile;
import static org.example.knockin.entity.room.QOfferRoomType.offerRoomType;
import static org.example.knockin.entity.room.QSeekerRoomType.seekerRoomType;
import static org.example.knockin.entity.room.QRoomType.roomType;
import static org.example.knockin.entity.room.QRegion.region;
import static org.example.knockin.entity.room.QRoomSeekerProfileRegion.roomSeekerProfileRegion;
import static org.example.knockin.entity.member.QState.state;
import static org.example.knockin.entity.member.QMemberPrivacy.memberPrivacy;
import static org.example.knockin.entity.auth.QAuthenticationApprove.authenticationApprove;
import static org.example.knockin.entity.auth.QAuthentication.authentication;
import static org.example.knockin.entity.member.QMemberDeclaration.memberDeclaration;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Member> findMemberByProvider(String providerId, LoginProviderType providerType) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(member)
                .where(providerIdEq(providerId), providerTypeEq(providerType))
                .fetchOne());
    }

    public Optional<AuthResponse> findMemberInfo(Member memberEntity) {
        // 기본 정보는 수정할 때마다 이력이 쌓인다. 로그인에서는 마지막 값 하나만 가져온다.
        QBasicInformation latestBasicInformation = new QBasicInformation("loginLatestBasicInformation");
        QBasicInformationFile latestBasicInformationFile = new QBasicInformationFile("loginLatestBasicInformationFile");
        QState latestState = new QState("loginLatestState");

        AuthResponse response = jpaQueryFactory
                .select(Projections.fields(AuthResponse.class,
                        member.id.as("memberId"),
                        basicInformation.name.as("name"),
                        basicInformation.birth.as("birth"),
                        basicInformation.gender.as("gender"),
                        file.savedFileName.as("profileImageUrl"),
                        memberPrivacy.type.as("visibility"),
                        JPAExpressions.selectOne()
                                .from(memberLifePattern)
                                .where(memberLifePattern.member.eq(member))
                                .exists()
                                .and(JPAExpressions.selectOne()
                                        .from(basicInformation)
                                        .where(basicInformation.member.eq(member))
                                        .exists())
                                .and(JPAExpressions.selectOne()
                                        .from(roomProfile)
                                        .where(roomProfile.member.eq(member))
                                        .exists())
                                .as("basicInfo"),
                        JPAExpressions.selectOne()
                                .from(preferenceCondition)
                                .where(preferenceCondition.member.eq(member))
                                .exists()
                                .and(JPAExpressions.selectOne()
                                                .from(preferenceConditionWeight)
                                                .where(preferenceConditionWeight.member.eq(member))
                                                .exists())
                                .as("preferenceInfo"),
                        Projections.fields(AuthResponse.DeleteInfo.class,
                                new CaseBuilder().when(member.isDelete.isTrue().or(state.states.eq(MemberState.INACTIVE)))
                                        .then(true).otherwise(false).as("isDelete"),
                                state.rejectReason.as("reason")).as("deleteInfo")
                ))
                .from(member)
                .leftJoin(basicInformation).on(
                        basicInformation.id.eq(
                                JPAExpressions
                                        .select(latestBasicInformation.id.max())
                                        .from(latestBasicInformation)
                                        .where(latestBasicInformation.member.eq(member))
                        )
                )
                .leftJoin(basicInformationFile).on(
                        basicInformationFile.id.eq(
                                JPAExpressions
                                        .select(latestBasicInformationFile.id.max())
                                        .from(latestBasicInformationFile)
                                        .where(latestBasicInformationFile.basicInformation.eq(basicInformation))
                        )
                )
                .leftJoin(basicInformationFile.file, file)
                .leftJoin(state).on(
                        state.id.eq(
                                JPAExpressions
                                        .select(latestState.id.max())
                                        .from(latestState)
                                        .where(latestState.member.eq(member))
                        )
                )
                .leftJoin(memberPrivacy).on(memberPrivacy.member.eq(member))
                .where(member.id.eq(memberEntity.getId()))
                .fetchOne();

        if (response != null && response.getDeleteInfo() != null) {
            if (memberEntity.isDelete()) {
                response.getDeleteInfo().setReason("탈퇴한 회원입니다.");
            }
        }

        return Optional.ofNullable(response);
    }

    @Override
    public List<Member> findByProfile(Member memberEntity) {
        return jpaQueryFactory.selectFrom(member)
                .leftJoin(basicInformation).on(basicInformation.member.eq(member))
                .leftJoin(basicInformationFile).on(basicInformationFile.basicInformation.eq(basicInformation))
                .leftJoin(basicInformationFile.file, file)
                .where(member.eq(memberEntity)).fetch();
    }

    @Override
    public List<MyProfileAllDto.Response.Lifestyle> findByLifePattern(Member memberEntity) {
        return jpaQueryFactory.select(Projections.fields(MyProfileAllDto.Response.Lifestyle.class,
                        memberLifePattern.id.as("id"),
                        lifePattern.id.as("lifestyleId"),
                        lifePattern.name.as("name"),
                        lifePatternInformation.dvalue.as("value"),
                        lifePatternInformation.description.as("description"),
                        lifePattern.dtype.as("type")
                ))
                .from(memberLifePattern)
                .leftJoin(memberLifePattern.lifePatternInformation, lifePatternInformation)
                .leftJoin(lifePatternInformation.lifePattern, lifePattern)
                .where(memberLifePattern.member.eq(memberEntity)).fetch();
    }

    @Override
    public Optional<RoomProfile> findByRoomProfile(Member memberEntity) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(roomProfile)
                .where(roomProfile.member.eq(memberEntity))
                .fetchOne());
    }

    @Override
    public List<MyProfileAllDto.Response.RoomProfile> findRoomTypes(RoomProfile profile) {
        if (profile instanceof RoomOfferProfile offer) {
            return jpaQueryFactory
                    .select(Projections.fields(MyProfileAllDto.Response.RoomProfile.class,
                            roomType.id.as("roomProfileId"),
                            roomType.name.as("roomProfileName")
                    ))
                    .from(offerRoomType)
                    .join(offerRoomType.roomType, roomType)
                    .where(offerRoomType.roomOfferProfile.eq(offer))
                    .fetch();

        } else if (profile instanceof RoomSeekerProfile seeker) {
            return jpaQueryFactory
                    .select(Projections.fields(MyProfileAllDto.Response.RoomProfile.class,
                            roomType.id.as("roomProfileId"),
                            roomType.name.as("roomProfileName")
                    ))
                    .from(seekerRoomType)
                    .join(seekerRoomType.roomType, roomType)
                    .where(seekerRoomType.roomSeekerProfile.eq(seeker))
                    .fetch();
        }

        return Collections.emptyList();
    }

    @Override
    public List<Region> findSeekerRegionEntities(RoomSeekerProfile seeker) {
        return jpaQueryFactory
                .select(region)
                .from(roomSeekerProfileRegion)
                .join(roomSeekerProfileRegion.region, region)
                .where(roomSeekerProfileRegion.roomSeekerProfile.eq(seeker))
                .fetch();
    }

    public List<MyPreferencesAllDto.Response.Lifestyle> findPreferenceLifeStyle(Member memberEntity) {
        return jpaQueryFactory.select(Projections.fields(MyPreferencesAllDto.Response.Lifestyle.class,
                        preferenceCondition.id.as("id"),
                        lifePattern.id.as("lifestyleId"),
                        lifePattern.name.as("name"),
                        lifePatternInformation.dvalue.as("value"),
                        lifePatternInformation.description.as("description"),
                        lifePattern.dtype.as("type")))
                .from(preferenceCondition)
                .join(preferenceCondition.lifePatternInformation, lifePatternInformation)
                .join(lifePatternInformation.lifePattern, lifePattern)
                .where(preferenceCondition.member.eq(memberEntity))
                .fetch();
    }

    public List<MyPreferencesAllDto.Response.Condition> findPreferenceCondition(Member memberEntity) {
        return jpaQueryFactory.select(Projections.fields(MyPreferencesAllDto.Response.Condition.class,
                        lifePattern.id.as("conditionsId"),
                        lifePattern.name.as("name")))
                .from(preferenceConditionWeight)
                .join(preferenceConditionWeight.lifePattern, lifePattern)
                .where(preferenceConditionWeight.member.eq(memberEntity))
                .fetch();
    }

    public Optional<Member> findByProviderId(String providerId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(member).where(member.providerId.eq(providerId)).fetchOne());
    }

    public List<Member> findMemberByDelete() {
        return jpaQueryFactory.selectFrom(member).where(member.isDelete.eq(true)).fetch();
    }

    @Override
    public List<MatchingBasicInfoRow> findMatchingBasicRow(List<Long> excludeMemberIds, Integer size) {
        if (size <= 0) return List.of();

        NumberExpression<Double> randomOrder = Expressions.numberTemplate(Double.class, "function('random')");

        return jpaQueryFactory
                .select(Projections.constructor(
                        MatchingBasicInfoRow.class,
                        member.id,
                        file.savedFileName,
                        basicInformation.name,
                        basicInformation.birth,
                        basicInformation.gender,
                        roomProfile.id,
                        roomProfile.type
                ))
                .from(member)
                .where(
                        member.isDelete.isFalse(),
                        memberPrivacy.type.eq(MemberPrivacyType.PUBLIC),
                        memberIdNotIn(excludeMemberIds),
                        notBlocked()
                )
                .join(member.memberPrivacy, memberPrivacy)
                .leftJoin(basicInformation)
                .on(basicInformation.id.eq(
                        JPAExpressions
                                .select(basicInformation.id.max())
                                .from(basicInformation)
                                .where(basicInformation.member.id.eq(member.id))
                ))
                .join(roomProfile)
                .on(roomProfile.id.eq(
                        JPAExpressions
                                .select(roomProfile.id.max())
                                .from(roomProfile)
                                .where(roomProfile.member.id.eq(member.id))
                ))
                .leftJoin(basicInformationFile)
                .on(basicInformationFile.id.eq(
                        JPAExpressions
                                .select(basicInformationFile.id.max())
                                .from(basicInformationFile)
                                .where(basicInformationFile.basicInformation.id.eq(basicInformation.id))
                ))
                .leftJoin(basicInformationFile.file, file)
                .on(file.isDeleted.isFalse())
                .orderBy(randomOrder.asc())
                .limit(size)
                .fetch();
    }

    @Override
    public Optional<MatchingBasicInfoRow> findMatchingBasicRowById(Long memberId) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.constructor(
                        MatchingBasicInfoRow.class,
                        member.id,
                        file.savedFileName,
                        basicInformation.name,
                        basicInformation.birth,
                        basicInformation.gender,
                        roomProfile.id,
                        roomProfile.type
                ))
                .from(member)
                .where(
                        member.id.eq(memberId),
                        member.isDelete.isFalse(),
                        memberPrivacy.type.eq(MemberPrivacyType.PUBLIC),
                        notBlocked()
                )
                .join(member.memberPrivacy, memberPrivacy)
                .leftJoin(basicInformation)
                .on(basicInformation.id.eq(
                        JPAExpressions
                                .select(basicInformation.id.max())
                                .from(basicInformation)
                                .where(basicInformation.member.id.eq(member.id))
                ))
                .join(roomProfile)
                .on(roomProfile.id.eq(
                        JPAExpressions
                                .select(roomProfile.id.max())
                                .from(roomProfile)
                                .where(roomProfile.member.id.eq(member.id))
                ))
                .leftJoin(basicInformationFile)
                .on(basicInformationFile.id.eq(
                        JPAExpressions
                                .select(basicInformationFile.id.max())
                                .from(basicInformationFile)
                                .where(basicInformationFile.basicInformation.id.eq(basicInformation.id))
                ))
                .leftJoin(basicInformationFile.file, file)
                .on(file.isDeleted.isFalse())
                .fetchOne());
    }

    @Override
    public List<BoMemberListDto.Response.MemberInfo> findBackOfficeMemberList(Pageable pageable, BoMemberListDto.Request request) {
        return jpaQueryFactory.select(Projections.fields(BoMemberListDto.Response.MemberInfo.class,
                    member.id,
                    basicInformation.name,
                    basicInformation.email,
                    member.createdAt,
                    member.role,
                    state.states.as("state"),
                    authenticationApprove.status.as("authenticationType")
                )).from(member)
                .leftJoin(basicInformation).on(basicInformation.member.eq(member))
                .leftJoin(state).on(state.member.eq(member))
                .leftJoin(authentication).on(authentication.member.eq(member))
                .leftJoin(authenticationApprove).on(authenticationApprove.authentication.eq(authentication))
                .where(searchName(request.getSearchName()), searchState(request.getSearchState()), searchApproveType(request.getSearchApproveType()))
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
    }

    @Override
    public BoMemberDetailDto.Response findBackOfficeMember(Long id) {
        return jpaQueryFactory
                .from(member)
                .leftJoin(basicInformation).on(basicInformation.member.eq(member))
                .leftJoin(state).on(state.member.eq(member))
                .leftJoin(authentication).on(authentication.member.eq(member))
                .where(member.id.eq(id))
                .transform(groupBy(member.id).as(Projections.fields(BoMemberDetailDto.Response.class,
                        member.id,
                        basicInformation.name,
                        basicInformation.email,
                        member.createdAt,
                        member.role,
                        basicInformation.gender,
                        basicInformation.birth,
                        state.states.as("state"),
                        ExpressionUtils.as(JPAExpressions
                                .select(memberDeclaration.count().intValue())
                                .from(memberDeclaration).where(memberDeclaration.reported.id.eq(member.id)), "reportCount"),
                        list(Projections.fields(BoMemberDetailDto.Response.AuthenticationInfo.class,
                                authentication.type.as("authenticationType"),
                                authentication.email.as("authenticationEmail"))).as("authenticationInfoList")))).get(id);
    }

    @Override
    public List<MemberWithNameRow> findAllWithNameRowById(List<Long> ids) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        MemberWithNameRow.class,
                        member.id,
                        basicInformation.name
                ))
                .from(member)
                .leftJoin(basicInformation)
                .on(basicInformation.id.eq(
                        JPAExpressions
                                .select(basicInformation.id.max())
                                .from(basicInformation)
                                .where(basicInformation.member.id.eq(member.id))
                ))
                .where(member.id.in(ids))
                .fetch();
    }

    private BooleanExpression providerIdEq(String providerId) {
        return StringUtils.hasText(providerId) ? member.providerId.eq(providerId) : null;
    }

    private BooleanExpression providerTypeEq(LoginProviderType providerType) {
        return providerType != null ? member.providerType.eq(providerType) : null;
    }

    private BooleanExpression memberIdNotIn(List<Long> memberIds) {
        if (memberIds == null || memberIds.isEmpty()) {
            return null;
        }

        List<Long> filteredIds = memberIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        return filteredIds.isEmpty() ? null : member.id.notIn(filteredIds);
    }

    private BooleanExpression notBlocked() {
        return JPAExpressions
                .selectOne()
                .from(block)
                .where(block.member.id.eq(member.id), block.isDeleted.isFalse())
                .notExists();
    }

    private BooleanExpression searchName(String name) {
        return StringUtils.hasText(name) ? basicInformation.name.contains(name) : null;
    }

    private BooleanExpression searchState(MemberState stateInfo) {
        return stateInfo != null ? state.states.eq(stateInfo) : null;
    }

    private BooleanExpression searchApproveType(ApproveType approveType) {
        return approveType != null ? authenticationApprove.status.eq(approveType) : null;
    }
}
