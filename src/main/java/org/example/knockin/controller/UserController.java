package org.example.knockin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.knockin.dto.*;
import org.example.knockin.global.api.CommonResponse;
import org.example.knockin.dto.PrincipalDetails;
import org.example.knockin.service.impl.MemberServiceImpl;
import org.example.knockin.service.impl.NotificationSettingServiceImpl;
import org.example.knockin.service.impl.OnBoardingServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
@Tag(name = "2. 온보딩/프로필")
public class UserController {
    private final MemberServiceImpl memberService;
    private final OnBoardingServiceImpl onBoardingService;
    private final NotificationSettingServiceImpl notificationSettingService;

    @DeleteMapping("")
    @Operation(summary = "회원 탈퇴")
    public CommonResponse<DeleteUserDto.Response> deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(memberService.deleteMember(principalDetails.getMember().getProviderId(), principalDetails.getMember().getProviderType()));
    }

    @PostMapping("/profile/basic")
    @Operation(summary = "기본정보 저장")
    public CommonResponse<SaveProfileBasicDto.Response> saveBasicInfo(@Valid @RequestBody SaveProfileBasicDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.saveBasicInfoLogic(request, principalDetails.getMember().getId()));
    }

    @PostMapping("/profile/lifestyle")
    @Operation(summary = "라이프스타일 저장")
    public CommonResponse<SaveProfileLifeStyleDto.Response> saveLifeStyle(@Valid @RequestBody SaveProfileLifeStyleDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.saveLifeStyleLogic(request, principalDetails.getMember().getId()));
    }

    @PostMapping("/profile/roominfo")
    @Operation(summary = "방 정보 저장")
    public CommonResponse<SaveProfileRoomInfoDto.Response> saveRoomInfo(@Valid @RequestBody SaveProfileRoomInfoDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.saveRoomInfoLogic(request, principalDetails.getMember().getId()));
    }

    @PostMapping("/profile/all")
    @Operation(summary = "전체 정보 저장")
    public CommonResponse<SaveProfileAllDto.Response> saveAll(@Valid @RequestBody SaveProfileAllDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.saveAll(request, principalDetails.getMember().getId()));
    }

    @PutMapping("/profile/basic")
    @Operation(summary = "기본정보 수정")
    public CommonResponse<ModifyProfileBasicDto.Response> modifyBasicInfo(@Valid @RequestBody ModifyProfileBasicDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.modifyBasicInfoLogic(request, principalDetails.getMember().getId()));
    }

    @PutMapping("/profile/lifestyle")
    @Operation(summary = "라이프스타일 수정")
    public CommonResponse<ModifyProfileLifeStyleDto.Response> modifyLifeStyle(@Valid @RequestBody ModifyProfileLifeStyleDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.modifyLifeStyleLogic(request, principalDetails.getMember().getId()));
    }

    @PutMapping("/profile/roominfo")
    @Operation(summary = "방 정보 수정")
    public CommonResponse<ModifyProfileRoomInfoDto.Response> modifyRoomInfo(@Valid @RequestBody ModifyProfileRoomInfoDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.modifyRoomInfoLogic(request, principalDetails.getMember().getId()));
    }

    @PutMapping("/profile/all")
    @Operation(summary = "전체 정보 수정")
    public CommonResponse<ModifyProfileAllDto.Response> modifyAll(@Valid @RequestBody ModifyProfileAllDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.modifyAll(request, principalDetails.getMember().getId()));
    }

    @PostMapping("/preferences/lifestyle")
    @Operation(summary = "선호 라이프스타일 저장")
    public CommonResponse<SavePreferencesLifeStyleDto.Response> savePreLifeStyle(@RequestBody SavePreferencesLifeStyleDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.savePreferenceLifeStyleLogic(request, principalDetails.getMember().getId()));
    }

    @PostMapping("/preferences/conditions")
    @Operation(summary = "선호 조건 저장")
    public CommonResponse<SavePreferencesConditionsDto.Response> savePreConditions(@RequestBody SavePreferencesConditionsDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.savePreferenceConditionLogic(request, principalDetails.getMember().getId()));
    }

    @PostMapping("/preferences/all")
    @Operation(summary = "선호 전체 저장")
    public CommonResponse<SavePreferencesAllDto.Response> savePreAll(@RequestBody SavePreferencesAllDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.savePreferenceAll(request, principalDetails.getMember().getId()));
    }

    @PutMapping("/preferences/lifestyle")
    @Operation(summary = "선호 라이프스타일 수정")
    public CommonResponse<ModifyPreferencesLifeStyleDto.Response> modifyPreLifeStyle(@RequestBody ModifyPreferencesLifeStyleDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.modifyPreLifeStyleLogic(request, principalDetails.getMember().getId()));
    }

    @PutMapping("/preferences/conditions")
    @Operation(summary = "선호 조건 수정")
    public CommonResponse<ModifyPreferencesConditionsDto.Response> modifyPreConditions(@RequestBody ModifyPreferencesConditionsDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.modifyPreConditionLogic(request, principalDetails.getMember().getId()));
    }

    @PutMapping("/preferences/all")
    @Operation(summary = "선호 전체 수정")
    public CommonResponse<ModifyPreferencesAllDto.Response> modifyPreAll(@RequestBody ModifyPreferencesAllDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.modifyPreAll(request, principalDetails.getMember().getId()));
    }

    @GetMapping("/preferences/all")
    @Operation(summary = "선호 전체 조회")
    public CommonResponse<MyPreferencesAllDto.Response> findPreAll(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.findPreAll(principalDetails.getMember().getId()));
    }

    @GetMapping("/profile/all")
    @Operation(summary = "내 프로필 전체 조회")
    public CommonResponse<MyProfileAllDto.Response> findProfileAll(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.findProfileAll(principalDetails.getMember().getId()));
    }

    @PatchMapping("/visibility")
    @Operation(summary = "프로필 공개 여부 변경")
    public CommonResponse<ProfileVisibilityDto.Response> changeProfileStatus(@RequestBody ProfileVisibilityDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.changeProfileStatus(request, principalDetails.getMember().getId()));
    }

    @GetMapping("/boards")
    @Operation(summary = "내가 쓴 게시글 조회")
    public CommonResponse<MyBoardListDto.Response> findMyBoardList(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(onBoardingService.findMyBoardList(pageable, principalDetails.getMember().getId()));
    }

    @GetMapping("/verifications")
    @Operation(summary = "내 인증 현황 조회")
    public CommonResponse<MyVerificationListDto.Response> findVerificationList(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return CommonResponse.status(HttpStatus.OK).body(new MyVerificationListDto.Response());
    }

    @GetMapping("/notification-settings")
    @Operation(summary = "알림 설정 조회")
    public CommonResponse<MyNotificationSettingsDto.Response> findAlaramSettingList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(notificationSettingService.findAlaramSettingList(principalDetails.getMember().getId()));
    }

    @PatchMapping("/notification-settings")
    @Operation(summary = "알림 설정 수정")
    public CommonResponse<AlarmSettingDto.Response> modifyAlaramSetting(@RequestBody AlarmSettingDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(notificationSettingService.modifyAlaramSetting(request, principalDetails.getMember().getId()));
    }
}
