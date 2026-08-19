package org.example.knockin.service.impl;

import org.example.knockin.dto.*;
import org.example.knockin.entity.auth.LoginProviderType;
import org.example.knockin.entity.member.BasicInformation;
import org.example.knockin.entity.member.Member;
import org.example.knockin.entity.member.DevicePlatform;
import org.example.knockin.entity.member.MemberRole;
import org.example.knockin.entity.member.MemberState;
import org.example.knockin.entity.member.State;
import org.example.knockin.exception.AuthErrorCode;
import org.example.knockin.exception.AuthException;
import org.example.knockin.exception.BusinessException;
import org.example.knockin.exception.MemberErrorCode;
import org.example.knockin.repository.alarm.AlarmSettingRepository;
import org.example.knockin.repository.life.MemberLifePatternRepository;
import org.example.knockin.repository.member.BasicInformationRepository;
import org.example.knockin.repository.member.MemberRepository;
import org.example.knockin.repository.member.StateRepository;
import org.example.knockin.repository.room.RoomProfileRepository;
import org.example.knockin.auth.service.Oauth2DeleteFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("회원 서비스 테스트")
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StateRepository stateRepository;

    @Mock
    private Oauth2DeleteFactory oauth2DeleteFactory;

    @Mock
    private MemberLifePatternRepository memberLifePatternRepository;

    @Mock
    private BasicInformationRepository basicInformationRepository;

    @Mock
    private RoomProfileRepository roomProfileRepository;

    @Mock
    private AlarmSettingRepository alarmSettingRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    @DisplayName("백오피스 회원 목록 조회 성공 테스트")
    void findBackOfficeMemberListSuccessTest() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        BoMemberListDto.Response.MemberInfo info = new BoMemberListDto.Response.MemberInfo();
        info.setId(1L);
        info.setName("홍길동");
        info.setEmail("test@test.com");
        BoMemberListDto.Request request = new BoMemberListDto.Request();

        given(memberRepository.findBackOfficeMemberList(pageable, request)).willReturn(List.of(info));

        // when
        BoMemberListDto.Response response = memberService.findBackOfficeMemberList(pageable, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMemberInfoList()).hasSize(1);
        assertThat(response.getMemberInfoList().get(0).getId()).isEqualTo(1L);
        assertThat(response.getMemberInfoList().get(0).getName()).isEqualTo("홍길동");
        verify(memberRepository).findBackOfficeMemberList(pageable, request);
    }

    @Test
    @DisplayName("백오피스 회원 상세 조회 성공 테스트")
    void findBackOfficeMemberSuccessTest() {
        // given
        Long memberId = 1L;
        BoMemberDetailDto.Response detail = new BoMemberDetailDto.Response();
        detail.setId(memberId);
        detail.setName("홍길동");

        given(memberRepository.findBackOfficeMember(memberId)).willReturn(detail);

        // when
        BoMemberDetailDto.Response response = memberService.findBackOfficeMember(memberId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(memberId);
        assertThat(response.getName()).isEqualTo("홍길동");
        verify(memberRepository).findBackOfficeMember(memberId);
    }

    @Test
    @DisplayName("회원 상태 수정 성공 테스트")
    void setMemberStateSuccessTest() {
        // given
        Member member = mock(Member.class);
        State state = spy(State.builder().states(MemberState.ACTIVE).build());

        given(stateRepository.findByMember(member)).willReturn(List.of(state));

        // when
        State result = memberService.setMemberState(member, MemberState.INACTIVE, "정지사유");

        // then
        assertThat(result).isNotNull();
        assertThat(result.getStates()).isEqualTo(MemberState.INACTIVE);
        verify(stateRepository).findByMember(member);
        verify(state).rejectState("정지사유");
    }

    @Test
    @DisplayName("회원 권한(Role) 수정 성공 테스트")
    void setMemberAuthSuccessTest() {
        // given
        Member member = spy(Member.builder().role(MemberRole.USER).build());

        // when
        Member result = memberService.setMemberAuth(member, MemberRole.ADMIN);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getRole()).isEqualTo(MemberRole.ADMIN);
        verify(member).changeRole(MemberRole.ADMIN);
    }

    @Test
    @DisplayName("FCM 디바이스 정보를 모두 저장한다")
    void upsertFcmPropsSuccessTest() {
        // given
        Long memberId = 1L;
        Member member = Member.builder().id(memberId).build();
        FcmDto.Request request = new FcmDto.Request();
        String maxLengthFcmToken = "t".repeat(512);
        request.setDeviceId("550e8400-e29b-41d4-a716-446655440000");
        request.setFcmToken(maxLengthFcmToken);
        request.setPlatform(DevicePlatform.ANDROID);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // when
        FcmDto.Response response = memberService.upsertFcmProps(memberId, request);

        // then
        assertThat(response.getUpdatedAt()).isNotNull();
        assertThat(member.getDeviceId()).isEqualTo(request.getDeviceId());
        assertThat(member.getFcmToken()).hasSize(512).isEqualTo(maxLengthFcmToken);
        assertThat(member.getPlatform()).isEqualTo(DevicePlatform.ANDROID);
        verify(memberRepository).findById(memberId);
    }

    @Test
    @DisplayName("존재하지 않는 회원의 FCM 디바이스 정보는 저장할 수 없다")
    void upsertFcmPropsFailsWhenMemberDoesNotExist() {
        // given
        Long memberId = 1L;
        FcmDto.Request request = new FcmDto.Request();
        request.setDeviceId("550e8400-e29b-41d4-a716-446655440000");
        request.setFcmToken("fcm-token");
        request.setPlatform(DevicePlatform.IOS);
        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.upsertFcmProps(memberId, request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", MemberErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("로그인 계정 권한(Role) 조회 성공 테스트")
    void findMyAccountRoleSuccessTest() {
        // given
        Long memberId = 1L;
        Member member = Member.builder().id(memberId).role(MemberRole.ADMIN).build();
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // when
        MyAccountDto.Response response = memberService.findMyAccountRole(memberId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getRole()).isEqualTo(MemberRole.ADMIN);
    }

    @Test
    @DisplayName("신규 SSO 회원 저장 시 기본정보에 이름과 이메일을 함께 저장한다")
    void getOrSaveCreatesBasicInformationFromSso() {
        OAuth2UserInfo userInfo = mock(OAuth2UserInfo.class);
        Member savedMember = Member.builder()
                .id(1L)
                .providerType(LoginProviderType.APPLE)
                .providerId("apple-provider-id")
                .role(MemberRole.USER)
                .build();
        given(userInfo.getId()).willReturn("apple-provider-id");
        given(userInfo.getProviderType()).willReturn(LoginProviderType.APPLE);
        given(userInfo.getName()).willReturn("  Apple User  ");
        given(userInfo.getEmail()).willReturn("  apple@example.com  ");
        given(memberRepository.findMemberByProvider("apple-provider-id", LoginProviderType.APPLE))
                .willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(savedMember);

        Member result = memberService.getOrSave(userInfo);

        ArgumentCaptor<BasicInformation> captor = ArgumentCaptor.forClass(BasicInformation.class);
        verify(basicInformationRepository).save(captor.capture());
        BasicInformation savedBasicInformation = captor.getValue();
        assertThat(result).isSameAs(savedMember);
        assertThat(savedBasicInformation.getMember()).isSameAs(savedMember);
        assertThat(savedBasicInformation.getName()).isEqualTo("Apple User");
        assertThat(savedBasicInformation.getEmail()).isEqualTo("apple@example.com");
        assertThat(savedBasicInformation.getBirth()).isNull();
        assertThat(savedBasicInformation.getGender()).isNull();
        verify(alarmSettingRepository).saveAll(any());
        verify(stateRepository).save(any(State.class));
    }

    @Test
    @DisplayName("기존 SSO 회원의 이름과 이메일은 재로그인 값으로 덮어쓰지 않는다")
    void getOrSaveKeepsExistingSocialInformationImmutable() {
        OAuth2UserInfo userInfo = mock(OAuth2UserInfo.class);
        Member member = Member.builder()
                .id(1L)
                .providerType(LoginProviderType.APPLE)
                .providerId("apple-provider-id")
                .role(MemberRole.USER)
                .build();
        BasicInformation basicInformation = BasicInformation.builder()
                .member(member)
                .name("최초 Apple 이름")
                .email("first@example.com")
                .build();
        given(userInfo.getId()).willReturn("apple-provider-id");
        given(userInfo.getProviderType()).willReturn(LoginProviderType.APPLE);
        given(memberRepository.findMemberByProvider("apple-provider-id", LoginProviderType.APPLE))
                .willReturn(Optional.of(member));
        given(basicInformationRepository.findByMember(member)).willReturn(List.of(basicInformation));

        Member result = memberService.getOrSave(userInfo);

        assertThat(result).isSameAs(member);
        assertThat(basicInformation.getName()).isEqualTo("최초 Apple 이름");
        assertThat(basicInformation.getEmail()).isEqualTo("first@example.com");
        verify(memberRepository, never()).save(any(Member.class));
        verify(basicInformationRepository, never()).save(any(BasicInformation.class));
    }

    @Test
    @DisplayName("기존 기본정보의 이름이나 이메일이 비어 있으면 SSO 값으로 보완한다")
    void getOrSaveBackfillsMissingSocialInformation() {
        OAuth2UserInfo userInfo = mock(OAuth2UserInfo.class);
        Member member = Member.builder()
                .id(1L)
                .providerType(LoginProviderType.KAKAO)
                .providerId("kakao-provider-id")
                .role(MemberRole.USER)
                .build();
        BasicInformation basicInformation = BasicInformation.builder()
                .member(member)
                .name(" ")
                .build();
        given(userInfo.getId()).willReturn("kakao-provider-id");
        given(userInfo.getProviderType()).willReturn(LoginProviderType.KAKAO);
        given(userInfo.getName()).willReturn("카카오 이름");
        given(userInfo.getEmail()).willReturn("kakao@example.com");
        given(memberRepository.findMemberByProvider("kakao-provider-id", LoginProviderType.KAKAO))
                .willReturn(Optional.of(member));
        given(basicInformationRepository.findByMember(member)).willReturn(List.of(basicInformation));

        memberService.getOrSave(userInfo);

        assertThat(basicInformation.getName()).isEqualTo("카카오 이름");
        assertThat(basicInformation.getEmail()).isEqualTo("kakao@example.com");
    }

    @Test
    @DisplayName("신규 SSO 회원의 이름 또는 이메일이 없으면 회원을 저장하지 않는다")
    void getOrSaveRejectsMissingRequiredSocialInformation() {
        OAuth2UserInfo userInfo = mock(OAuth2UserInfo.class);
        given(userInfo.getId()).willReturn("apple-provider-id");
        given(userInfo.getProviderType()).willReturn(LoginProviderType.APPLE);
        given(userInfo.getName()).willReturn(" ");
        given(memberRepository.findMemberByProvider("apple-provider-id", LoginProviderType.APPLE))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.getOrSave(userInfo))
                .isInstanceOf(AuthException.class)
                .hasFieldOrPropertyWithValue("errorCode", AuthErrorCode.SSO_USER_INFO_OMISSION);
        verify(memberRepository, never()).save(any(Member.class));
        verify(basicInformationRepository, never()).save(any(BasicInformation.class));
    }
}
