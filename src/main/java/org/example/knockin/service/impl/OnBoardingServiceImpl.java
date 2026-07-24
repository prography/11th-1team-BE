package org.example.knockin.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.knockin.dto.*;
import org.example.knockin.entity.agreement.AgreementLog;
import org.example.knockin.entity.agreement.MemberAgreement;
import org.example.knockin.entity.life.*;
import org.example.knockin.entity.member.BasicInformation;
import org.example.knockin.entity.member.Member;
import org.example.knockin.entity.member.MemberPrivacy;
import org.example.knockin.entity.member.MemberPrivacyType;
import org.example.knockin.entity.room.*;
import org.example.knockin.exception.AuthErrorCode;
import org.example.knockin.exception.BusinessException;
import org.example.knockin.exception.MetaErrorCode;
import org.example.knockin.exception.OnBoardErrorCode;
import org.example.knockin.service.RoommateBoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OnBoardingServiceImpl {
    private final BasicInformationServiceImpl basicInformationService;
    private final MemberServiceImpl memberService;
    private final MemberAgreementServiceImpl memberAgreementService;
    private final MemberLifePatternService memberLifePatternService;
    private final RoomProfileServiceImpl roomProfileService;
    private final RoomTypeServiceImpl roomTypeService;
    private final RoomSeekerProfileRegionServiceImpl roomSeekerProfileRegionService;
    private final MetaServiceImpl metaService;
    private final PreferenceConditionServiceImpl preferenceConditionService;
    private final LifeStyleServiceImpl lifeStyleService;
    private final MemberPrivacyServiceImpl memberPrivacyService;
    private final MyRoomMateServiceImpl myRoomMateService;
    private final RoommateBoardService roommateBoardService;

    @Transactional
    public BasicInformation saveBasicInfo(SaveProfileBasicDto.Request request, Member member) {
        BasicInformation basicInformation = BasicInformation.builder().member(member).name(request.getName()).birth(request.getBirth()).gender(request.getGender()).email(request.getEmail()).build();
        return basicInformationService.save(basicInformation);
    }

    @Transactional
    public List<MemberAgreement> saveMemberAgreement(SaveProfileBasicDto.Request request, Member member) {
        List<MemberAgreement> memberAgreementList = new ArrayList<>();

        metaService.findByAgreementLogIsCurrent(request.getTerms()).forEach(item -> {
            memberAgreementList.add(MemberAgreement.builder().member(member).agreementLog(item).isAgreed(true).build());
        });

        return memberAgreementService.saveAll(memberAgreementList);
    }

    @Transactional
    public SaveProfileBasicDto.Response saveBasicInfoLogic(SaveProfileBasicDto.Request request, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        if (memberService.hasBasicInformation(member)) {
            throw new BusinessException(OnBoardErrorCode.ONBOARD_PROFILE_ALREADY_EXISTS);
        }
        if(ObjectUtils.isEmpty(saveBasicInfo(request, member))) throw new BusinessException(OnBoardErrorCode.ONBOARD_BASIC_SAVE_ERROR);
        if(saveMemberAgreement(request, member).isEmpty()) throw new BusinessException(OnBoardErrorCode.ONBOARD_TERM_SAVE_ERROR);
        saveOrUpdateState(member);
        return SaveProfileBasicDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public List<MemberLifePattern> saveMemberLifeStyle(SaveProfileLifeStyleDto.Request request, Member member) {
        List<MemberLifePattern> memberLifePatternList = new ArrayList<>();
        List<MemberLifePatternLog> memberLifePatternLogList = new ArrayList<>();

        metaService.findByLifeStyle(request.getLifestyles()).forEach(item -> {
            memberLifePatternList.add(MemberLifePattern.builder().member(member).lifePatternInformation(item).build());
            memberLifePatternLogList.add(MemberLifePatternLog.builder().member(member).lifePatternInformation(item).build());
        });


        memberLifePatternService.saveMemberLifePatternLogAll(memberLifePatternLogList);
        return memberLifePatternService.saveMemberLifePatternAll(memberLifePatternList);
    }

    @Transactional
    public SaveProfileLifeStyleDto.Response saveLifeStyleLogic(SaveProfileLifeStyleDto.Request request, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        if (memberService.hasLifestyle(member)) {
            throw new BusinessException(OnBoardErrorCode.ONBOARD_PROFILE_ALREADY_EXISTS);
        }
        if(saveMemberLifeStyle(request, member).isEmpty()) throw new BusinessException(OnBoardErrorCode.ONBOARD_LIFE_STYLE_SAVE_ERROR);
        saveOrUpdateState(member);
        return SaveProfileLifeStyleDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public RoomProfile saveRoomInfo(SaveProfileRoomInfoDto.Request request, Member member) {
        RoomProfile roomProfile = null;

        switch (request.getType()) {
            case OFFER -> {
                List<OfferRoomType> offerRoomTypeList = new ArrayList<>();

                Region region = metaService.findByRegionId(request.getRegion().getFirst()).orElseThrow(() -> new BusinessException(MetaErrorCode.REGION_NOT_FOUND));
                RoomOfferProfile roomOfferProfile = roomProfileService.save(RoomOfferProfile.builder()
                        .member(member)
                        .region(region)
                        .deposit(request.getDeposit())
                        .monthlyRent(request.getMounthRent())
                        .isComeableAtNegotiable(request.isComeableAtNegotiable())
                        .comeableAt(request.getComeEnableAt()).build());
                roomProfile = roomOfferProfile;

                metaService.findByRoomTypes(request.getRoomProfile()).forEach(item -> {
                    offerRoomTypeList.add(OfferRoomType.builder().roomType(item).roomOfferProfile(roomOfferProfile).build());
                });
                roomTypeService.saveOfferRoomTypeAll(offerRoomTypeList);
            }
            case SEEKER -> {
                List<SeekerRoomType> seekerRoomTypes = new ArrayList<>();
                List<RoomSeekerProfileRegion> roomSeekerProfileRegionList = new ArrayList<>();

                RoomSeekerProfile roomSeekerProfile = roomProfileService.save(RoomSeekerProfile.builder()
                        .member(member)
                        .minDeposit(request.getMinDeposit())
                        .maxDeposit(request.getMaxDeposit())
                        .minMonthlyRent(request.getMinMounthRent())
                        .maxMonthlyRent(request.getMaxMounthRent())
                        .isComeableAtNegotiable(request.isComeableAtNegotiable())
                        .comeableAt(request.getComeEnableAt()).build());
                roomProfile = roomSeekerProfile;

                metaService.findByRegions(request.getRegion()).forEach(item -> {
                    roomSeekerProfileRegionList.add(RoomSeekerProfileRegion.builder().roomSeekerProfile(roomSeekerProfile).region(item).build());
                });
                roomSeekerProfileRegionService.saveAll(roomSeekerProfileRegionList);

                metaService.findByRoomTypes(request.getRoomProfile()).forEach(item -> {
                    seekerRoomTypes.add(SeekerRoomType.builder().roomType(item).roomSeekerProfile(roomSeekerProfile).build());
                });
                roomTypeService.saveSeekerRoomTypeAll(seekerRoomTypes);
            }
        }

        return roomProfile;
    }

    @Transactional
    public SaveProfileRoomInfoDto.Response saveRoomInfoLogic(SaveProfileRoomInfoDto.Request request, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        validateRoomProfile(request);
        if (memberService.hasRoomProfile(member)) {
            throw new BusinessException(OnBoardErrorCode.ONBOARD_PROFILE_ALREADY_EXISTS);
        }
        if(ObjectUtils.isEmpty(saveRoomInfo(request,member))) throw new BusinessException(OnBoardErrorCode.ONBOARD_ROOM_INFO_SAVE_ERROR);
        saveOrUpdateState(member);
        return SaveProfileRoomInfoDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public SaveProfileAllDto.Response saveAll(SaveProfileAllDto.Request request, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        validateRoomProfile(request);
        // POST는 최초 저장용이다. 이미 값이 있으면 덮지 않고 수정 API를 쓰도록 알려준다.
        if (memberService.hasAnyOnboardingData(member)) {
            throw new BusinessException(OnBoardErrorCode.ONBOARD_PROFILE_ALREADY_EXISTS);
        }

        SaveProfileBasicDto.Request basicRequest = SaveProfileBasicDto.Request.builder()
                .name(request.getName())
                .birth(request.getBirth())
                .gender(request.getGender())
                .email(request.getEmail())
                .terms(request.getTerms())
                .build();

        SaveProfileLifeStyleDto.Request lifeStyleRequest = SaveProfileLifeStyleDto.Request.builder()
                .lifestyles(request.getLifestyles())
                .build();

        SaveProfileRoomInfoDto.Request roomInfoRequest = SaveProfileRoomInfoDto.Request.builder()
                .type(request.getType())
                .minDeposit(request.getMinDeposit())
                .maxDeposit(request.getMaxDeposit())
                .minMounthRent(request.getMinMounthRent())
                .maxMounthRent(request.getMaxMounthRent())
                .comeEnableAt(request.getComeEnableAt())
                .region(request.getRegion())
                .roomProfile(request.getRoomProfile())
                .deposit(request.getDeposit())
                .mounthRent(request.getMounthRent())
                .isComeableAtNegotiable(request.isComeableAtNegotiable())
                .build();

        if(ObjectUtils.isEmpty(saveBasicInfo(basicRequest, member))) throw new BusinessException(OnBoardErrorCode.ONBOARD_BASIC_SAVE_ERROR);
        if(saveMemberAgreement(basicRequest, member).isEmpty()) throw new BusinessException(OnBoardErrorCode.ONBOARD_TERM_SAVE_ERROR);
        if(saveMemberLifeStyle(lifeStyleRequest, member).isEmpty()) throw new BusinessException(OnBoardErrorCode.ONBOARD_LIFE_STYLE_SAVE_ERROR);
        if(ObjectUtils.isEmpty(saveRoomInfo(roomInfoRequest,member))) throw new BusinessException(OnBoardErrorCode.ONBOARD_ROOM_INFO_SAVE_ERROR);
        saveOrUpdateState(member);

        return SaveProfileAllDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    private void validateRoomProfile(RoomProfileInput request) {
        if (request.getType() == null) {
            throw new BusinessException(OnBoardErrorCode.ONBOARD_PROFILE_VALIDATION_FAIL);
        }

        boolean invalidOffer = request.getType() == RoomProfileType.OFFER
                && (request.getDeposit() == null || request.getMounthRent() == null);
        boolean invalidSeeker = request.getType() == RoomProfileType.SEEKER
                && (request.getMinDeposit() == null
                || request.getMaxDeposit() == null
                || request.getMinMounthRent() == null
                || request.getMaxMounthRent() == null
                || request.getMinDeposit() > request.getMaxDeposit()
                || request.getMinMounthRent() > request.getMaxMounthRent());

        if (invalidOffer || invalidSeeker) {
            throw new BusinessException(OnBoardErrorCode.ONBOARD_PROFILE_VALIDATION_FAIL);
        }
    }

    @Transactional
    public void modifyBasicInfo(ModifyProfileBasicDto.Request request, Member member) {
        BasicInformation basicInformation = basicInformationService.findLatestBasicInformation(member);
        basicInformation.modifyBasicInformation(request);
    }

    @Transactional
    public void modifyAgreement(ModifyProfileBasicDto.Request request, Member member) {
        List<AgreementLog> requestAgreementList = metaService.findByAgreementLogIsCurrent(request.getTerms());
        List<AgreementLog> memberAgreementList = memberAgreementService.findByMember(member).stream().map(MemberAgreement::getAgreementLog).toList();

        Set<Long> memberAgreementIds = memberAgreementList.stream().map(AgreementLog::getId).collect(Collectors.toSet());
        List<AgreementLog> skipList = requestAgreementList.stream().filter(reqLog -> memberAgreementIds.contains(reqLog.getId())).toList();

        if (skipList.isEmpty()) {
            memberAgreementService.findByMember(member).forEach(MemberAgreement::disableAgree);
        } else {
            memberAgreementService.findByMemberAndAgreementLogNotIn(member, skipList).forEach(MemberAgreement::disableAgree);
        }

        requestAgreementList.forEach(item -> {
            boolean isNotInSkipList = skipList.stream().noneMatch(skipItem -> Objects.equals(skipItem.getId(), item.getId()));

            if (isNotInSkipList) {
                memberAgreementService.save(MemberAgreement.builder()
                        .member(member)
                        .agreementLog(item)
                        .isAgreed(true)
                        .build());
            }
        });
    }

    @Transactional
    public ModifyProfileBasicDto.Response modifyBasicInfoLogic(ModifyProfileBasicDto.Request request, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));

        modifyBasicInfo(request, member);
        modifyAgreement(request, member);

        return ModifyProfileBasicDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public void modifyLifeStyle(ModifyProfileLifeStyleDto.Request request, Member member) {
        List<Long> memberLifePatternIds = request.getLifestyles().stream()
                .map(ModifyProfileLifeStyleDto.Request.LifeStyleInfo::getId)
                .toList();

        List<Long> newLifestyleIds = request.getLifestyles().stream()
                .map(ModifyProfileLifeStyleDto.Request.LifeStyleInfo::getLifestyleId)
                .toList();

        Map<Long, LifePatternInformation> newInfoMap = lifeStyleService.findLifePatternInformationAllById(newLifestyleIds).stream()
                .collect(Collectors.toMap(LifePatternInformation::getId, info -> info));

        List<MemberLifePattern> memberLifePatternList = memberLifePatternService.findByMember(member);
        List<MemberLifePattern> modifyMemberLifePatternList = memberLifePatternList.stream()
                .filter(item -> memberLifePatternIds.contains(item.getId()))
                .toList();

        modifyMemberLifePatternList.forEach(item -> {
            request.getLifestyles().forEach(data -> {
                if (Objects.equals(data.getId(), item.getId())) {
                    LifePatternInformation newInfo = newInfoMap.get(data.getLifestyleId());

                    if(!Objects.equals(newInfo.getLifePattern().getId(), item.getLifePatternInformation().getLifePattern().getId())) {
                        throw new BusinessException(OnBoardErrorCode.ONBOARD_LIFE_STYLE_VAILDATION_FAIL);
                    }

                    item.modifyLifePatternInformation(newInfo);
                }
            });
        });
    }

    @Transactional
    public void modifyLifeStyleLog(Member member) {
        List<MemberLifePattern> memberLifePatternList = memberLifePatternService.findByMember(member);
        List<MemberLifePatternLog> logList = memberLifePatternList.stream().map(pattern ->
                MemberLifePatternLog.builder().member(member).lifePatternInformation(pattern.getLifePatternInformation()).build()).toList();

        if (!logList.isEmpty()) {
            memberLifePatternService.saveMemberLifePatternLogAll(logList);
        }
    }

    @Transactional
    public ModifyProfileLifeStyleDto.Response modifyLifeStyleLogic(ModifyProfileLifeStyleDto.Request request, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        modifyLifeStyle(request, member);
        modifyLifeStyleLog(member);
        return ModifyProfileLifeStyleDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public void modifyRoomInfo(ModifyProfileRoomInfoDto.Request request, Member member) {
        RoomProfile roomProfile = roomProfileService.findByMember(member).getFirst();
        RoomProfileType targetType = request.getType();

        if (roomProfile.getType() != targetType) {
            if (roomProfile instanceof RoomOfferProfile roomOfferProfile) {
                roomTypeService.deleteByRoomOfferProfile(roomOfferProfile);
            } else if (roomProfile instanceof RoomSeekerProfile seekerProfile) {
                roomTypeService.deleteByRoomSeekerProfile(seekerProfile);
                roomSeekerProfileRegionService.deleteByRoomSeekerProfile(seekerProfile);
            }

            roomProfileService.delete(roomProfile);

            if (targetType == RoomProfileType.SEEKER) {
                RoomSeekerProfile newSeekerProfile = roomProfileService.save(RoomSeekerProfile.builder()
                        .member(member)
                        .minDeposit(request.getMinDeposit())
                        .maxDeposit(request.getMaxDeposit())
                        .minMonthlyRent(request.getMinMounthRent())
                        .maxMonthlyRent(request.getMaxMounthRent())
                        .isComeableAtNegotiable(request.isComeableAtNegotiable())
                        .comeableAt(request.getComeEnableAt())
                        .build());

                List<RoomSeekerProfileRegion> seekerRegions = metaService.findByRegions(request.getRegion()).stream().map(region -> RoomSeekerProfileRegion.builder().roomSeekerProfile(newSeekerProfile).region(region).build()).toList();
                roomSeekerProfileRegionService.saveAll(seekerRegions);

                List<SeekerRoomType> seekerRoomTypes = metaService.findByRoomTypes(request.getRoomProfile()).stream().map(roomType -> SeekerRoomType.builder().roomSeekerProfile(newSeekerProfile).roomType(roomType).build()).toList();
                roomTypeService.saveSeekerRoomTypeAll(seekerRoomTypes);
            } else if (targetType == RoomProfileType.OFFER) {
                Region region = metaService.findByRegionId(request.getRegion().getFirst())
                        .orElseThrow(() -> new BusinessException(MetaErrorCode.REGION_NOT_FOUND));

                RoomOfferProfile newOfferProfile = roomProfileService.save(RoomOfferProfile.builder()
                        .member(member)
                        .region(region)
                        .deposit(request.getDeposit())
                        .monthlyRent(request.getMounthRent())
                        .isComeableAtNegotiable(request.isComeableAtNegotiable())
                        .comeableAt(request.getComeEnableAt())
                        .build());

                List<OfferRoomType> offerRoomTypes = metaService.findByRoomTypes(request.getRoomProfile()).stream().map(roomType -> OfferRoomType.builder().roomOfferProfile(newOfferProfile).roomType(roomType).build()).toList();
                roomTypeService.saveOfferRoomTypeAll(offerRoomTypes);
            }
        } else {
            List<RoomType> roomTypeList = metaService.findByRoomTypes(request.getRoomProfile());

            if (roomProfile instanceof RoomOfferProfile roomOfferProfile) {
                Region region = metaService.findByRegionId(request.getRegion().getFirst()).orElseThrow(() -> new BusinessException(MetaErrorCode.REGION_NOT_FOUND));

                roomOfferProfile.updateOffer(request, region);
                roomTypeService.deleteByRoomOfferProfile(roomOfferProfile);

                List<OfferRoomType> offerRoomTypeList = roomTypeList.stream().map(item -> OfferRoomType.builder().roomType(item).roomOfferProfile(roomOfferProfile).build()).toList();
                roomTypeService.saveOfferRoomTypeAll(offerRoomTypeList);
            } else if (roomProfile instanceof RoomSeekerProfile seekerProfile) {
                List<Region> regionList = metaService.findByRegions(request.getRegion());

                seekerProfile.updateSeeker(request);
                roomTypeService.deleteByRoomSeekerProfile(seekerProfile);

                List<SeekerRoomType> seekerRoomTypes = roomTypeList.stream().map(item -> SeekerRoomType.builder().roomType(item).roomSeekerProfile(seekerProfile).build()).toList();
                roomTypeService.saveSeekerRoomTypeAll(seekerRoomTypes);

                roomSeekerProfileRegionService.deleteByRoomSeekerProfile(seekerProfile);

                List<RoomSeekerProfileRegion> roomSeekerProfileRegionList = regionList.stream().map(item -> RoomSeekerProfileRegion.builder().roomSeekerProfile(seekerProfile).region(item).build()).toList();
                roomSeekerProfileRegionService.saveAll(roomSeekerProfileRegionList);
            } else {
                throw new BusinessException(OnBoardErrorCode.ONBOARD_ROOM_INFO_VAILDATION_FAIL);
            }
        }
    }

    @Transactional
    public ModifyProfileRoomInfoDto.Response modifyRoomInfoLogic(ModifyProfileRoomInfoDto.Request request, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        validateRoomProfile(request);
        modifyRoomInfo(request, member);
        return ModifyProfileRoomInfoDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public ModifyProfileAllDto.Response modifyAll(ModifyProfileAllDto.Request request, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        validateRoomProfile(request);

        ModifyProfileBasicDto.Request basicRequest = ModifyProfileBasicDto.Request.builder()
                .name(request.getName())
                .birth(request.getBirth())
                .gender(request.getGender())
                .email(request.getEmail())
                .terms(request.getTerms())
                .build();

        List<ModifyProfileLifeStyleDto.Request.LifeStyleInfo> mappedLifestyles = request.getLifestyles().stream()
                .map(info -> {
                    ModifyProfileLifeStyleDto.Request.LifeStyleInfo targetInfo = new ModifyProfileLifeStyleDto.Request.LifeStyleInfo();
                    targetInfo.setId(info.getId());
                    targetInfo.setLifestyleId(info.getLifestyleId());
                    return targetInfo;
                }).toList();

        ModifyProfileLifeStyleDto.Request lifeStyleRequest = ModifyProfileLifeStyleDto.Request.builder()
                .lifestyles(mappedLifestyles)
                .build();

        ModifyProfileRoomInfoDto.Request roomInfoRequest = ModifyProfileRoomInfoDto.Request.builder()
                .type(request.getType())
                .minDeposit(request.getMinDeposit())
                .maxDeposit(request.getMaxDeposit())
                .minMounthRent(request.getMinMounthRent())
                .maxMounthRent(request.getMaxMounthRent())
                .comeEnableAt(request.getComeEnableAt())
                .isComeableAtNegotiable(request.isComeableAtNegotiable())
                .region(request.getRegion())
                .roomProfile(request.getRoomProfile())
                .deposit(request.getDeposit())
                .mounthRent(request.getMounthRent())
                .build();

        modifyBasicInfo(basicRequest, member);
        modifyAgreement(basicRequest, member);
        modifyLifeStyle(lifeStyleRequest, member);
        modifyLifeStyleLog(member);
        modifyRoomInfo(roomInfoRequest, member);

        return ModifyProfileAllDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public List<PreferenceCondition> savePreferenceLifeStyle(SavePreferencesLifeStyleDto.Request request, Member member) {
        List<PreferenceCondition> preferenceConditionList = new ArrayList<>();
        metaService.findByLifeStyle(request.getLifestyles()).forEach(item ->
                preferenceConditionList.add(PreferenceCondition.builder().member(member).lifePatternInformation(item).build()));
        return preferenceConditionService.preferenceConditionSaveAll(preferenceConditionList);
    }

    @Transactional
    public List<PreferenceConditionLog> savePreferenceLifeStyleLog(SavePreferencesLifeStyleDto.Request request, Member member) {
        List<PreferenceConditionLog> preferenceConditionLogList = new ArrayList<>();
        metaService.findByLifeStyle(request.getLifestyles()).forEach(item ->
                preferenceConditionLogList.add(PreferenceConditionLog.builder().member(member).lifePatternInformation(item).build()));
        return preferenceConditionService.preferenceConditionLogSaveAll(preferenceConditionLogList);
    }

    @Transactional
    public SavePreferencesLifeStyleDto.Response savePreferenceLifeStyleLogic(SavePreferencesLifeStyleDto.Request request, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));

        if(savePreferenceLifeStyle(request, member).isEmpty()) throw new BusinessException(OnBoardErrorCode.ONBOARD_PREFERENCE_STEP1_SAVE_ERROR);
        if(savePreferenceLifeStyleLog(request, member).isEmpty()) throw new BusinessException(OnBoardErrorCode.ONBOARD_PREFERENCE_STEP1_LOG_SAVE_ERROR);

        return SavePreferencesLifeStyleDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public List<PreferenceConditionWeight>  savePreferenceCondition(SavePreferencesConditionsDto.Request request, Member member) {
        List<PreferenceConditionWeight> preferenceConditionWeightList = new ArrayList<>();
        metaService.findLifePatternByLifeStyle(request.getConditions()).forEach(item ->
                preferenceConditionWeightList.add(PreferenceConditionWeight.builder().member(member).lifePattern(item).build()));
        return preferenceConditionService.preferenceConditionWeightSaveAll(preferenceConditionWeightList);
    }

    @Transactional
    public List<PreferenceConditionWeightLog>  savePreferenceConditionLog(SavePreferencesConditionsDto.Request request, Member member) {
        List<PreferenceConditionWeightLog> preferenceConditionWeightLogList = new ArrayList<>();
        metaService.findLifePatternByLifeStyle(request.getConditions()).forEach(item ->
                preferenceConditionWeightLogList.add(PreferenceConditionWeightLog.builder().member(member).lifePattern(item).build()));
        return preferenceConditionService.preferenceConditionWeightLogSaveAll(preferenceConditionWeightLogList);
    }

    @Transactional
    public SavePreferencesConditionsDto.Response savePreferenceConditionLogic(SavePreferencesConditionsDto.Request request, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));

        if(savePreferenceCondition(request, member).isEmpty()) throw new BusinessException(OnBoardErrorCode.ONBOARD_PREFERENCE_STEP2_SAVE_ERROR);
        if(savePreferenceConditionLog(request, member).isEmpty()) throw new BusinessException(OnBoardErrorCode.ONBOARD_PREFERENCE_STEP2_LOG_SAVE_ERROR);

        return SavePreferencesConditionsDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public SavePreferencesAllDto.Response savePreferenceAll(SavePreferencesAllDto.Request request, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));

        SavePreferencesLifeStyleDto.Request lifeStyleRequest = new SavePreferencesLifeStyleDto.Request(request.getLifestyles());
        SavePreferencesConditionsDto.Request conditionRequest = new SavePreferencesConditionsDto.Request(request.getConditions());

        if(savePreferenceLifeStyle(lifeStyleRequest, member).isEmpty()) throw new BusinessException(OnBoardErrorCode.ONBOARD_PREFERENCE_STEP1_SAVE_ERROR);
        if(savePreferenceLifeStyleLog(lifeStyleRequest, member).isEmpty()) throw new BusinessException(OnBoardErrorCode.ONBOARD_PREFERENCE_STEP1_LOG_SAVE_ERROR);
        if(savePreferenceCondition(conditionRequest, member).isEmpty()) throw new BusinessException(OnBoardErrorCode.ONBOARD_PREFERENCE_STEP2_SAVE_ERROR);
        if(savePreferenceConditionLog(conditionRequest, member).isEmpty()) throw new BusinessException(OnBoardErrorCode.ONBOARD_PREFERENCE_STEP2_LOG_SAVE_ERROR);

        return SavePreferencesAllDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public void modifyPreferenceLifeStyle(ModifyPreferencesLifeStyleDto.Request request, Member member) {
        Map<Long, LifePatternInformation> newInfoMap = request.getLifestyles().stream().collect(Collectors.toMap(ModifyPreferencesLifeStyleDto.Request.LifeStyleInfo::getId, item -> lifeStyleService.findLifePatternInformationById(item.getLifestyleId())));
        preferenceConditionService.findPreferenceConditionByMember(member).forEach(item -> {
            request.getLifestyles().forEach(data -> {
                if(Objects.equals(data.getId(), item.getId())) {
                    LifePatternInformation newInfo = newInfoMap.get(data.getId());

                    if(!Objects.equals(newInfo.getLifePattern().getId(), item.getLifePatternInformation().getLifePattern().getId())) {
                        throw new BusinessException(OnBoardErrorCode.ONBOARD_LIFE_STYLE_VAILDATION_FAIL);
                    }

                    item.modifyLifePatternInformation(newInfoMap.get(item.getId()));
                }
            });
        });
    }

    @Transactional
    public void modifyPreferenceLifeStyleLog(Member member) {
        List<PreferenceCondition> preferenceConditionList = preferenceConditionService.findPreferenceConditionByMember(member);
        List<PreferenceConditionLog> logList = preferenceConditionList.stream().map(pattern ->
                PreferenceConditionLog.builder().member(member).lifePatternInformation(pattern.getLifePatternInformation()).build()).toList();

        if (!logList.isEmpty()) {
            preferenceConditionService.preferenceConditionLogSaveAll(logList);
        }
    }

    @Transactional
    public ModifyPreferencesLifeStyleDto.Response modifyPreLifeStyleLogic(ModifyPreferencesLifeStyleDto.Request request, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));

        modifyPreferenceLifeStyle(request,member);
        modifyPreferenceLifeStyleLog(member);

        return ModifyPreferencesLifeStyleDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public void modifyPreCondition(ModifyPreferencesConditionsDto.Request request, Member member) {
        preferenceConditionService.deletePreferenceConditionWeightByMember(member);

        List<PreferenceConditionWeight> preferenceConditionWeightList = new ArrayList<>();
        lifeStyleService.findAllById(request.getConditions()).forEach(item -> preferenceConditionWeightList.add(PreferenceConditionWeight.builder().member(member).lifePattern(item).build()));
        preferenceConditionService.preferenceConditionWeightSaveAll(preferenceConditionWeightList);
    }

    @Transactional
    public void modifyPreConditionLog(Member member) {
        List<PreferenceConditionWeight> preferenceConditionWeightList = preferenceConditionService.findPreferenceConditionWeightByMember(member);
        List<PreferenceConditionWeightLog> logList = preferenceConditionWeightList.stream().map(patternWeight ->
                PreferenceConditionWeightLog.builder().member(member).lifePattern(patternWeight.getLifePattern()).build()).toList();

        if (!logList.isEmpty()) {
            preferenceConditionService.preferenceConditionWeightLogSaveAll(logList);
        }
    }

    @Transactional
    public ModifyPreferencesConditionsDto.Response modifyPreConditionLogic(ModifyPreferencesConditionsDto.Request request, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));

        modifyPreCondition(request, member);
        modifyPreConditionLog(member);

        return ModifyPreferencesConditionsDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    @Transactional
    public ModifyPreferencesAllDto.Response modifyPreAll(ModifyPreferencesAllDto.Request request, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));

        ModifyPreferencesLifeStyleDto.Request lifeStyleRequest = ModifyPreferencesLifeStyleDto.Request.builder().lifestyles(request.getLifestyles()).build();
        ModifyPreferencesConditionsDto.Request conditionRequest = ModifyPreferencesConditionsDto.Request.builder().conditions(request.getConditions()).build();

        modifyPreferenceLifeStyle(lifeStyleRequest,member);
        modifyPreferenceLifeStyleLog(member);
        modifyPreCondition(conditionRequest, member);
        modifyPreConditionLog(member);

        return ModifyPreferencesAllDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    public MyProfileAllDto.Response findProfileAll(Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        return memberService.findProfileAll(member);
    }

    public MyPreferencesAllDto.Response findPreAll(Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        return memberService.findPreAll(member);
    }

    @Transactional
    public ProfileVisibilityDto.Response changeProfileStatus(ProfileVisibilityDto.Request request, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));

        if(myRoomMateService.isExistRoomMate(member)) throw new BusinessException(OnBoardErrorCode.ONBOARD_PROFILE_STATE_CHANGE_ERROR);
        memberPrivacyService.findByMember(member).getFirst().changeState(request.getStatus());

        return ProfileVisibilityDto.Response.builder().updatedAt(LocalDateTime.now()).build();
    }

    public MyBoardListDto.Response findMyBoardList(Pageable pageable, Long memberId) {
        Member member = memberService.findById(memberId).orElseThrow(() -> new BusinessException(AuthErrorCode.MEMBER_NOT_FOUND));
        Page<MyBoardListDto.Response.BoardItem> pageResult = roommateBoardService.getMyBoardList(pageable, member);
        return MyBoardListDto.Response.builder().boards(pageResult.getContent()).build();
    }

    @Transactional
    public void saveOrUpdateState(Member member) {
        MemberPrivacyType targetType = memberService.isOnBoarding(member) ? MemberPrivacyType.PUBLIC : MemberPrivacyType.PRIVATE;

        List<MemberPrivacy> privacyList = memberPrivacyService.findByMember(member);
        if (!privacyList.isEmpty()) {
            privacyList.getFirst().changeState(targetType);
        }
        else {
            memberPrivacyService.save(MemberPrivacy.builder().member(member).type(targetType).build());
        }
    }
}
