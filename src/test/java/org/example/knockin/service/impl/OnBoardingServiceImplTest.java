package org.example.knockin.service.impl;
 
import org.example.knockin.dto.*;
import org.example.knockin.entity.agreement.AgreementLog;
import org.example.knockin.entity.agreement.MemberAgreement;
import org.example.knockin.entity.life.*;
import org.example.knockin.entity.member.BasicInformation;
import org.example.knockin.entity.member.Member;
import org.example.knockin.entity.member.MemberPrivacy;
import org.example.knockin.entity.member.MemberPrivacyType;
import org.example.knockin.entity.room.*;
import org.example.knockin.exception.BusinessException;
import org.example.knockin.exception.OnBoardErrorCode;
import org.example.knockin.service.RoommateBoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;
 
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
class OnBoardingServiceImplTest {
 
    @Mock private BasicInformationServiceImpl basicInformationService;
    @Mock private MemberServiceImpl memberService;
    @Mock private MemberAgreementServiceImpl memberAgreementService;
    @Mock private MemberLifePatternService memberLifePatternService;
    @Mock private RoomProfileServiceImpl roomProfileService;
    @Mock private RoomTypeServiceImpl roomTypeService;
    @Mock private RoomSeekerProfileRegionServiceImpl roomSeekerProfileRegionService;
    @Mock private MetaServiceImpl metaService;
    @Mock private PreferenceConditionServiceImpl preferenceConditionService;
    @Mock private LifeStyleServiceImpl lifeStyleService;
    @Mock private MemberPrivacyServiceImpl memberPrivacyService;
    @Mock private MyRoomMateServiceImpl myRoomMateService;
    @Mock private RoommateBoardService roommateBoardService;
 
    @InjectMocks
    private OnBoardingServiceImpl onBoardingService;
 
    private Member member;
    private Long memberId = 1L;
 
    @BeforeEach
    void setUp() {
        member = Member.builder().id(memberId).build();
        lenient().when(memberService.findById(memberId)).thenReturn(Optional.of(member));
        lenient().when(memberService.isOnBoarding(any())).thenReturn(true);
        lenient().when(memberPrivacyService.findByMember(any())).thenReturn(Collections.emptyList());
    }
 
    @Test
    @DisplayName("기본 정보 저장 성공 테스트")
    void saveBasicInfoLogic_Success() {
        SaveProfileBasicDto.Request request = SaveProfileBasicDto.Request.builder()
                .name("홍길동")
                .terms(List.of(1L))
                .build();
 
        given(basicInformationService.save(any())).willReturn(mock(BasicInformation.class));
        given(metaService.findByAgreementLogIsCurrent(any())).willReturn(List.of(mock(AgreementLog.class)));
        given(memberAgreementService.saveAll(any())).willReturn(List.of(mock(MemberAgreement.class)));
 
        SaveProfileBasicDto.Response response = onBoardingService.saveBasicInfoLogic(request, memberId);
 
        assertThat(response).isNotNull();
        verify(basicInformationService).save(any());
        verify(memberAgreementService).saveAll(any());
        verify(memberPrivacyService).save(any());
    }
 
    @Test
    @DisplayName("라이프스타일 저장 성공 테스트")
    void saveLifeStyleLogic_Success() {
        SaveProfileLifeStyleDto.Request request = SaveProfileLifeStyleDto.Request.builder()
                .lifestyles(List.of(1L))
                .build();
 
        given(metaService.findByLifeStyle(any())).willReturn(List.of(mock(LifePatternInformation.class)));
        given(memberLifePatternService.saveMemberLifePatternAll(any())).willReturn(List.of(mock(MemberLifePattern.class)));
 
        SaveProfileLifeStyleDto.Response response = onBoardingService.saveLifeStyleLogic(request, memberId);
 
        assertThat(response).isNotNull();
        verify(memberLifePatternService).saveMemberLifePatternAll(any());
    }
 
    @Test
    @DisplayName("방 정보 저장 성공 테스트 (OFFER)")
    void saveRoomInfoLogic_Offer_Success() {
        SaveProfileRoomInfoDto.Request request = SaveProfileRoomInfoDto.Request.builder()
                .type(RoomProfileType.OFFER)
                .deposit(500)
                .mounthRent(50)
                .region(List.of(1L))
                .roomProfile(List.of(1L))
                .build();
 
        given(metaService.findByRegionId(anyLong())).willReturn(Optional.of(mock(Region.class)));
        given(roomProfileService.save(any(RoomOfferProfile.class))).willReturn(mock(RoomOfferProfile.class));
        given(metaService.findByRoomTypes(any())).willReturn(List.of(mock(RoomType.class)));
 
        SaveProfileRoomInfoDto.Response response = onBoardingService.saveRoomInfoLogic(request, memberId);
 
        assertThat(response).isNotNull();
        verify(roomProfileService).save(any(RoomOfferProfile.class));
        verify(roomTypeService).saveOfferRoomTypeAll(any());
    }
 
    @Test
    @DisplayName("방 정보 저장 성공 테스트 (SEEKER)")
    void saveRoomInfoLogic_Seeker_Success() {
        SaveProfileRoomInfoDto.Request request = SaveProfileRoomInfoDto.Request.builder()
                .type(RoomProfileType.SEEKER)
                .minDeposit(100)
                .maxDeposit(500)
                .minMounthRent(10)
                .maxMounthRent(50)
                .region(List.of(1L))
                .roomProfile(List.of(1L))
                .build();
 
        given(roomProfileService.save(any(RoomSeekerProfile.class))).willReturn(mock(RoomSeekerProfile.class));
        given(metaService.findByRegions(any())).willReturn(List.of(mock(Region.class)));
        given(metaService.findByRoomTypes(any())).willReturn(List.of(mock(RoomType.class)));
 
        SaveProfileRoomInfoDto.Response response = onBoardingService.saveRoomInfoLogic(request, memberId);
 
        assertThat(response).isNotNull();
        verify(roomProfileService).save(any(RoomSeekerProfile.class));
        verify(roomSeekerProfileRegionService).saveAll(any());
        verify(roomTypeService).saveSeekerRoomTypeAll(any());
    }

    @Test
    @DisplayName("방 정보 저장 시 최소 금액이 최대 금액보다 크면 거부한다")
    void saveRoomInfoLogic_RejectsInvalidRange() {
        SaveProfileRoomInfoDto.Request request = SaveProfileRoomInfoDto.Request.builder()
                .type(RoomProfileType.SEEKER)
                .minDeposit(500)
                .maxDeposit(100)
                .minMounthRent(50)
                .maxMounthRent(10)
                .region(List.of(1L))
                .roomProfile(List.of(1L))
                .build();

        assertThatThrownBy(() -> onBoardingService.saveRoomInfoLogic(request, memberId))
                .isInstanceOfSatisfying(
                        BusinessException.class,
                        exception -> assertThat(exception.getErrorCode())
                                .isEqualTo(OnBoardErrorCode.ONBOARD_PROFILE_VALIDATION_FAIL)
                );

        verifyNoInteractions(roomProfileService, roomTypeService, roomSeekerProfileRegionService);
    }
 
    @Test
    @DisplayName("전체 프로필 저장 성공 테스트")
    void saveAll_Success() {
        SaveProfileAllDto.Request request = new SaveProfileAllDto.Request();
        request.setType(RoomProfileType.OFFER);
        request.setTerms(List.of(1L));
        request.setLifestyles(List.of(1L));
        request.setRegion(List.of(1L));
        request.setRoomProfile(List.of(1L));
        request.setDeposit(1000);
        request.setMounthRent(50);
 
        given(basicInformationService.save(any())).willReturn(mock(BasicInformation.class));
        given(metaService.findByAgreementLogIsCurrent(any())).willReturn(List.of(mock(AgreementLog.class)));
        given(memberAgreementService.saveAll(any())).willReturn(List.of(mock(MemberAgreement.class)));
        given(metaService.findByLifeStyle(any())).willReturn(List.of(mock(LifePatternInformation.class)));
        given(memberLifePatternService.saveMemberLifePatternAll(any())).willReturn(List.of(mock(MemberLifePattern.class)));
        given(metaService.findByRegionId(anyLong())).willReturn(Optional.of(mock(Region.class)));
        given(roomProfileService.save(any(RoomOfferProfile.class))).willReturn(mock(RoomOfferProfile.class));
        given(metaService.findByRoomTypes(any())).willReturn(List.of(mock(RoomType.class)));
 
        SaveProfileAllDto.Response response = onBoardingService.saveAll(request, memberId);
 
        assertThat(response).isNotNull();
        verify(basicInformationService).save(any());
        verify(memberLifePatternService).saveMemberLifePatternAll(any());
        verify(roomProfileService).save(any(RoomOfferProfile.class));
    }

    @Test
    @DisplayName("이미 온보딩 데이터가 있으면 전체 프로필을 중복 저장하지 않는다")
    void saveAll_RejectsDuplicateProfile() {
        SaveProfileAllDto.Request request = new SaveProfileAllDto.Request();
        request.setType(RoomProfileType.OFFER);
        request.setDeposit(1000);
        request.setMounthRent(50);
        given(memberService.hasAnyOnboardingData(member)).willReturn(true);

        assertThatThrownBy(() -> onBoardingService.saveAll(request, memberId))
                .isInstanceOfSatisfying(
                        BusinessException.class,
                        exception -> assertThat(exception.getErrorCode())
                                .isEqualTo(OnBoardErrorCode.ONBOARD_PROFILE_ALREADY_EXISTS)
                );

        verifyNoInteractions(
                basicInformationService,
                memberAgreementService,
                memberLifePatternService,
                roomProfileService
        );
    }
 
    @Test
    @DisplayName("기본 정보 수정 성공 테스트")
    void modifyBasicInfoLogic_Success() {
        ModifyProfileBasicDto.Request request = ModifyProfileBasicDto.Request.builder()
                .name("이몽룡")
                .terms(List.of(1L))
                .build();
 
        BasicInformation basicInfo = mock(BasicInformation.class);
        given(basicInformationService.findLatestBasicInformation(member)).willReturn(basicInfo);
        given(metaService.findByAgreementLogIsCurrent(any())).willReturn(List.of(mock(AgreementLog.class)));
        given(memberAgreementService.findByMember(member)).willReturn(Collections.emptyList());
 
        ModifyProfileBasicDto.Response response = onBoardingService.modifyBasicInfoLogic(request, memberId);
 
        assertThat(response).isNotNull();
        verify(basicInfo).modifyBasicInformation(request);
    }
 
    @Test
    @DisplayName("프로필 공개 여부 변경 성공 테스트")
    void changeProfileStatus_Success() {
        ProfileVisibilityDto.Request request = new ProfileVisibilityDto.Request();
        request.setStatus(MemberPrivacyType.PUBLIC);
 
        given(myRoomMateService.isExistRoomMate(member)).willReturn(false);
        MemberPrivacy privacy = mock(MemberPrivacy.class);
        given(memberPrivacyService.findByMember(member)).willReturn(List.of(privacy));
 
        ProfileVisibilityDto.Response response = onBoardingService.changeProfileStatus(request, memberId);
 
        assertThat(response).isNotNull();
        verify(privacy).changeState(MemberPrivacyType.PUBLIC);
    }
 
    @Test
    @DisplayName("룸메이트가 있을 때 프로필 상태 변경 시 예외 발생")
    void changeProfileStatus_Fail_WhenRoomMateExists() {
        ProfileVisibilityDto.Request request = new ProfileVisibilityDto.Request();
        given(myRoomMateService.isExistRoomMate(member)).willReturn(true);
 
        assertThatThrownBy(() -> onBoardingService.changeProfileStatus(request, memberId))
                .isInstanceOf(BusinessException.class);
    }
 
    @Test
    @DisplayName("라이프스타일 수정 성공 테스트")
    void modifyLifeStyleLogic_Success() {
        ModifyProfileLifeStyleDto.Request.LifeStyleInfo info = new ModifyProfileLifeStyleDto.Request.LifeStyleInfo();
        info.setId(1L);
        info.setLifestyleId(10L);
        ModifyProfileLifeStyleDto.Request request = ModifyProfileLifeStyleDto.Request.builder()
                .lifestyles(List.of(info))
                .build();
 
        LifePattern lifePattern = mock(LifePattern.class);
        given(lifePattern.getId()).willReturn(100L);
 
        LifePatternInformation oldInfo = mock(LifePatternInformation.class);
        given(oldInfo.getLifePattern()).willReturn(lifePattern);
 
        MemberLifePattern memberLifePattern = mock(MemberLifePattern.class);
        given(memberLifePattern.getId()).willReturn(1L);
        given(memberLifePattern.getLifePatternInformation()).willReturn(oldInfo);
 
        LifePatternInformation newInfo = mock(LifePatternInformation.class);
        given(newInfo.getLifePattern()).willReturn(lifePattern);
        given(newInfo.getId()).willReturn(10L);
 
        given(lifeStyleService.findLifePatternInformationAllById(any())).willReturn(List.of(newInfo));
        given(memberLifePatternService.findByMember(member)).willReturn(List.of(memberLifePattern));
 
        ModifyProfileLifeStyleDto.Response response = onBoardingService.modifyLifeStyleLogic(request, memberId);
 
        assertThat(response).isNotNull();
        verify(memberLifePattern).modifyLifePatternInformation(newInfo);
    }
 
    @Test
    @DisplayName("방 정보 수정 테스트 (OFFER -> SEEKER 전환)")
    void modifyRoomInfoLogic_Switch_Success() {
        ModifyProfileRoomInfoDto.Request request = ModifyProfileRoomInfoDto.Request.builder()
                .type(RoomProfileType.SEEKER)
                .minDeposit(100)
                .maxDeposit(500)
                .minMounthRent(10)
                .maxMounthRent(50)
                .region(List.of(1L))
                .roomProfile(List.of(1L))
                .build();
 
        RoomOfferProfile oldProfile = mock(RoomOfferProfile.class);
        given(oldProfile.getType()).willReturn(RoomProfileType.OFFER);
        given(roomProfileService.findByMember(member)).willReturn(List.of(oldProfile));
        given(roomProfileService.save(any(RoomSeekerProfile.class))).willReturn(mock(RoomSeekerProfile.class));
        given(metaService.findByRegions(any())).willReturn(List.of(mock(Region.class)));
        given(metaService.findByRoomTypes(any())).willReturn(List.of(mock(RoomType.class)));
 
        ModifyProfileRoomInfoDto.Response response = onBoardingService.modifyRoomInfoLogic(request, memberId);
 
        assertThat(response).isNotNull();
        verify(roomTypeService).deleteByRoomOfferProfile(oldProfile);
        verify(roomProfileService).delete(oldProfile);
        verify(roomProfileService).save(any(RoomSeekerProfile.class));
    }
 
    @Test
    @DisplayName("선호 조건 저장 성공 테스트")
    void savePreferenceConditionLogic_Success() {
        SavePreferencesConditionsDto.Request request = new SavePreferencesConditionsDto.Request(List.of(1L));
 
        given(metaService.findLifePatternByLifeStyle(any())).willReturn(List.of(mock(LifePattern.class)));
        given(preferenceConditionService.preferenceConditionWeightSaveAll(any())).willReturn(List.of(mock(PreferenceConditionWeight.class)));
        given(preferenceConditionService.preferenceConditionWeightLogSaveAll(any())).willReturn(List.of(mock(PreferenceConditionWeightLog.class)));
 
        SavePreferencesConditionsDto.Response response = onBoardingService.savePreferenceConditionLogic(request, memberId);
 
        assertThat(response).isNotNull();
        verify(preferenceConditionService).preferenceConditionWeightSaveAll(any());
    }
 
    @Test
    @DisplayName("내가 쓴 게시글 조회 테스트")
    void findMyBoardList_Success() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<MyBoardListDto.Response.BoardItem> page = new PageImpl<>(List.of(mock(MyBoardListDto.Response.BoardItem.class)));
        given(roommateBoardService.getMyBoardList(any(), any())).willReturn(page);
 
        MyBoardListDto.Response response = onBoardingService.findMyBoardList(pageable, memberId);
 
        assertThat(response.getBoards()).hasSize(1);
    }
}
