package org.example.knockin.verification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.knockin.verification.dto.EmailConfirmDto;
import org.example.knockin.verification.dto.EmailSendDto;
import org.example.knockin.global.api.CommonResponse;
import org.example.knockin.global.auth.dto.PrincipalDetails;
import org.example.knockin.verification.service.impl.AuthenticationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/verify")
@Tag(name = "7. 신원인증/차단")
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;

    @PostMapping("/student/send")
    @Operation(summary = "학생 이메일 인증번호 발송")
    public CommonResponse<EmailSendDto.Response> sendAuthNumStudent(@RequestBody EmailSendDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(authenticationService.sendAuthNumStudent(request, principalDetails.getMember().getId()));
    }

    @PostMapping("/company/send")
    @Operation(summary = "직장인 이메일 인증번호 발송")
    public CommonResponse<EmailSendDto.Response> sendAuthNumCompany(@RequestBody EmailSendDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(authenticationService.sendAuthNumCompany(request, principalDetails.getMember().getId()));
    }

    @PostMapping("/student/confirm")
    @Operation(summary = "학생 이메일 인증번호 확인")
    public CommonResponse<EmailConfirmDto.Response> confirmAuthNumStudent(@RequestBody EmailConfirmDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(authenticationService.confirmAuthNumStudent(request, principalDetails.getMember().getId()));
    }

    @PostMapping("/company/confirm")
    @Operation(summary = "직장인 이메일 인증번호 확인")
    public CommonResponse<EmailConfirmDto.Response> confirmAuthNumCompany(@RequestBody EmailConfirmDto.Request request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return CommonResponse.status(HttpStatus.OK).body(authenticationService.confirmAuthNumCompany(request, principalDetails.getMember().getId()));
    }

    @PostMapping("/webhook")
    public CommonResponse<?> handleWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "svix-id", required = false) String svixId,
            @RequestHeader(value = "svix-timestamp", required = false) String svixTimestamp,
            @RequestHeader(value = "svix-signature", required = false) String svixSignature) {
        Map<String, String> headers = new HashMap<>();
        headers.put("svix-id", svixId);
        headers.put("svix-timestamp", svixTimestamp);
        headers.put("svix-signature", svixSignature);

        String message = authenticationService.mailWebHook(payload, headers);
        return CommonResponse.status(HttpStatus.OK).body(message);
    }
}

