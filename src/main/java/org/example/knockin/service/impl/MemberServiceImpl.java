package org.example.knockin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.knockin.dto.*;
import org.example.knockin.entity.alarm.Alarm;
import org.example.knockin.entity.alarm.AlarmSetting;
import org.example.knockin.entity.alarm.AlarmSettingType;
import org.example.knockin.entity.alarm.AlarmType;
import org.example.knockin.entity.auth.LoginProviderType;
import org.example.knockin.entity.member.*;
import org.example.knockin.entity.room.*;
import org.example.knockin.dto.AuthResponse;
import org.example.knockin.dto.OAuth2UserInfo;
import org.example.knockin.exception.AuthErrorCode;
import org.example.knockin.auth.service.Oauth2DeleteFactory;
import org.example.knockin.exception.BusinessException;
import org.example.knockin.exception.MemberErrorCode;
import org.example.knockin.repository.alarm.AlarmSettingRepository;
import org.example.knockin.repository.life.MemberLifePatternRepository;
import org.example.knockin.repository.member.BasicInformationRepository;
import org.example.knockin.repository.member.MemberRepository;
import org.example.knockin.repository.member.row.MatchingBasicInfoRow;
import org.example.knockin.repository.member.row.MemberWithNameRow;
import org.example.knockin.repository.member.StateRepository;
import org.example.knockin.repository.room.RoomProfileRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberServiceImpl {
    private final MemberRepository memberRepository;
    private final Oauth2DeleteFactory oauth2DeleteFactory;
    private final MemberLifePatternRepository memberLifePatternRepository;
    private final BasicInformationRepository basicInformationRepository;
    private final RoomProfileRepository roomProfileRepository;
    private final StateRepository stateRepository;
    private final AlarmSettingRepository alarmSettingRepository;
    private final AlarmServiceImpl alarmService;
    private final PushNotificationServiceImpl pushNotificationService;

    @Transactional
    public Member getOrSave(OAuth2UserInfo oAuth2UserInfo) {
        String providerId = String.valueOf(oAuth2UserInfo.getId());
        return memberRepository.findMemberByProvider(providerId, oAuth2UserInfo.getProviderType())
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .providerType(oAuth2UserInfo.getProviderType())
                            .providerId(String.valueOf(oAuth2UserInfo.getId()))
                            .email(oAuth2UserInfo.getEmail())
                            .role(MemberRole.USER)
                            .name(oAuth2UserInfo.getName())
                            .isDelete(false)
                            .build();

                    Member resultMember = memberRepository.save(newMember);
                    List<AlarmSetting> alarmSettingList = Arrays.stream(AlarmSettingType.values()).map(item -> AlarmSetting.builder().member(resultMember).isEnabled(true).alarmSettingType(item).build()).toList();
                    alarmSettingRepository.saveAll(alarmSettingList);
                    stateRepository.save(State.builder().states(MemberState.ACTIVE).member(resultMember).build());
                    return resultMember;
                });
    }

    public AuthResponse findMemberForLogin(Member member, String accessToken) {
        AuthResponse authResponse = memberRepository.findMemberInfo(member).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        authResponse.setAccessToken(accessToken);
        return authResponse;
    }

    @Transactional
    public DeleteUserDto.Response deleteMember(String userName, LoginProviderType providerType) {
        Member member = memberRepository.findMemberByProvider(userName, providerType).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));

        if(oauth2DeleteFactory.getDeleteService(member.getProviderType()).requestUnlink(member.getProviderId())) {
            member.delete();
        } else {
            throw new BusinessException(AuthErrorCode.OAUTH_UNLINK_FAIL);
        }

        return DeleteUserDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public void hardDeleteMember() {
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(5);

        List<Member> memberList = memberRepository.findMemberByDelete();
        List<Member> membersToDelete = memberList.stream().filter(item -> item.getDeletedAt() != null && item.getDeletedAt().isBefore(thresholdDate)).toList();

        if (!membersToDelete.isEmpty()) {
            memberRepository.deleteAll(membersToDelete);
        }
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Member findByIdOrThrow(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public List<MemberWithNameRow> findAllWithNameRowById(List<Long> ids) {
        return memberRepository.findAllWithNameRowById(ids);
    }

    public List<MatchingBasicInfoRow> findMatchingBasicRow(
            List<Long> excludeMemberIds,
            int limit,
            Long likedByMemberId,
            Long requesterId
    ) {
        return memberRepository.findMatchingBasicRow(
                excludeMemberIds,
                limit,
                likedByMemberId,
                requesterId
        );
    }

    public MatchingBasicInfoRow findMatchingBasicRowById(Long memberId) {
        return memberRepository.findMatchingBasicRowById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public MyProfileAllDto.Response findProfileAll(Member member) {
        MyProfileAllDto.Response.UserInfo userInfo = memberRepository.findByProfile(member);
        if (userInfo.getBirth() != null) {
            userInfo.setAge(Period.between(userInfo.getBirth(), LocalDate.now()).getYears());
        }
        List<MyProfileAllDto.Response.Lifestyle> lifestyles = memberRepository.findByLifePattern(member);
        List<MyProfileAllDto.Response.Region> regions = new ArrayList<>();
        List<MyProfileAllDto.Response.RoomProfile> roomProfiles = new ArrayList<>();
        RoomProfileType type = null;
        LocalDateTime comeEnableAt = null;
        Integer deposit = null;
        Integer mounthRent = null;
        Integer minDeposit = null;
        Integer maxDeposit = null;
        Integer minMounthRent = null;
        Integer maxMounthRent = null;

        RoomProfile roomProfileEntity = memberRepository.findByRoomProfile(member).orElse(null);
        if (roomProfileEntity != null) {
            type = roomProfileEntity.getType();
            comeEnableAt = roomProfileEntity.getComeableAt();

            if (roomProfileEntity instanceof RoomOfferProfile offer) {
                deposit = offer.getDeposit();
                mounthRent = offer.getMonthlyRent();

                if (offer.getRegion() != null) {
                    MyProfileAllDto.Response.Region regDto = new MyProfileAllDto.Response.Region();
                    regDto.setRegionId(offer.getRegion().getId());
                    regDto.setRegion(getFullRegionName(offer.getRegion()));
                    regions.add(regDto);
                }

                roomProfiles = memberRepository.findRoomTypes(offer);
            } else if (roomProfileEntity instanceof RoomSeekerProfile seeker) {
                minDeposit = seeker.getMinDeposit();
                maxDeposit = seeker.getMaxDeposit();
                minMounthRent = seeker.getMinMonthlyRent();
                maxMounthRent = seeker.getMaxMonthlyRent();

                List<Region> seekerRegions = memberRepository.findSeekerRegionEntities(seeker);
                for (Region region : seekerRegions) {
                    MyProfileAllDto.Response.Region regDto = new MyProfileAllDto.Response.Region();
                    regDto.setRegionId(region.getId());
                    regDto.setRegion(getFullRegionName(region));
                    regions.add(regDto);
                }
                roomProfiles = memberRepository.findRoomTypes(seeker);
            }
        }

        return MyProfileAllDto.Response.builder()
                .lifestyles(lifestyles)
                .type(type)
                .comeEnableAt(comeEnableAt)
                .deposit(deposit)
                .mounthRent(mounthRent)
                .minDeposit(minDeposit)
                .maxDeposit(maxDeposit)
                .minMounthRent(minMounthRent)
                .maxMounthRent(maxMounthRent)
                .region(regions)
                .roomProfile(roomProfiles)
                .userInfo(userInfo)
                .build();
    }

    public MyPreferencesAllDto.Response findPreAll(Member member) {
        return MyPreferencesAllDto.Response.builder()
                .lifestyles(memberRepository.findPreferenceLifeStyle(member))
                .conditions(memberRepository.findPreferenceCondition(member))
                .build();
    }

    public boolean isOnBoarding(Member member) {
        return roomProfileRepository.isExsitRoomProfile(member) && memberLifePatternRepository.isExsitLifeStyle(member) && basicInformationRepository.isExsitBasicInformation(member);
    }

    private String getFullRegionName(Region regionEntity) {
        if (regionEntity == null) {
            return "";
        }

        List<String> regionNames = new ArrayList<>();
        Region current = regionEntity;

        while (current != null) {
            regionNames.add(0, current.getName());
            current = current.getParent();
        }

        return String.join(" ", regionNames);
    }

    public BoMemberListDto.Response findBackOfficeMemberList(Pageable pageable, BoMemberListDto.Request request) {
        return BoMemberListDto.Response.builder().memberInfoList(memberRepository.findBackOfficeMemberList(pageable, request)).build();
    }

    public BoMemberDetailDto.Response findBackOfficeMember(Long id) {
        return memberRepository.findBackOfficeMember(id);
    }

    @Transactional
    public State setMemberState(Member member, MemberState memberState, String rejectReason) {
        State state = stateRepository.findByMember(member).getFirst();

        if(memberState.equals(MemberState.ACTIVE)) {
            state.activeState();
            Alarm alarm = Alarm.builder()
                    .title(MemberAlarmTemplate.MEMBER_ACTIVE.formatTitle())
                    .contents(MemberAlarmTemplate.MEMBER_ACTIVE.formatContents())
                    .isRead(false)
                    .member(member)
                    .expiredAt(LocalDateTime.now().plusDays(1))
                    .type(AlarmType.DEFAULT)
                    .build();

            alarmService.sendToClient(member.getId(), MemberAlarmTemplate.MEMBER_ACTIVE.name(), alarm);
            pushNotificationService.send(member, AlarmSettingType.NOTIFICATION, MemberAlarmTemplate.MEMBER_ACTIVE.formatTitle(), MemberAlarmTemplate.MEMBER_ACTIVE.formatContents(), MemberAlarmTemplate.MEMBER_ACTIVE.formatDeepLink());
        } else {
            state.rejectState(rejectReason);
        }

        return state;
    }

    @Transactional
    public Member setMemberAuth(Member member, MemberRole memberRole) {
        member.changeRole(memberRole);
        return member;
    }

    @Transactional
    public FcmDto.Response upsertFcmProps(Long memberId, FcmDto.Request request) {
        Member member = findByIdOrThrow(memberId);
        member.setFcmProps(request.getDeviceId(), request.getFcmToken(), request.getPlatform());
        return FcmDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    public MyProfileAllDto.Response.UserInfo findProfileInfo(Member member) {
        return memberRepository.findByProfile(member);
    }

    public MyAccountDto.Response findMyAccountRole(Long memberId) {
        Member member = findByIdOrThrow(memberId);
        return MyAccountDto.Response.builder().role(member.getRole()).build();
    }

    public State findStateByMemberId(Long memberId) {
        return stateRepository.findByMemberId(memberId).getFirst();
    }

    public void validateMemberState(Long memberId) {
        State state = findStateByMemberId(memberId);
        if (!state.getStates().equals(MemberState.ACTIVE)) {
            throw new BusinessException(MemberErrorCode.NOT_ACTIVE_MEMBER);
        }
    }

    @Transactional
    public LogOutDto.Response logout(Long id) {
        Member member = findByIdOrThrow(id);
        member.clearFcmProps();
        return LogOutDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }
}
