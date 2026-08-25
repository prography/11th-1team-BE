package org.example.knockin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.knockin.member.dto.BlockDto;
import org.example.knockin.member.dto.BlockDto.Response;
import org.example.knockin.member.dto.BlockListDto;
import org.example.knockin.global.auth.dto.PrincipalDetails;
import org.example.knockin.global.api.CommonResponse;
import org.example.knockin.member.service.impl.BlockServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blocks")
@Tag(name = "7. 신원인증/차단")
@RequiredArgsConstructor
public class BlockController {
    private final BlockServiceImpl blockServiceImpl;

    @PostMapping("")
    @Operation(summary = "차단 저장")
    public CommonResponse<BlockDto.Response> saveBlock(
            @AuthenticationPrincipal PrincipalDetails details,
            @RequestBody BlockDto.Request request
    ) {
        Response response = blockServiceImpl.saveBlock(details.getMember().getId(), request.getUserId());
        return CommonResponse.status(HttpStatus.OK).body(response);
    }

    @GetMapping("")
    @Operation(summary = "차단 목록 조회")
    public CommonResponse<BlockListDto.Response> findBlockList(
            @AuthenticationPrincipal PrincipalDetails details,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        BlockListDto.Response response = blockServiceImpl.findMyList(details.getMember().getId(), pageable);
        return CommonResponse.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{blockId}")
    @Operation(summary = "차단 해제")
    public CommonResponse<BlockDto.Response> deleteBlock(
            @AuthenticationPrincipal PrincipalDetails details,
            @PathVariable Long blockId
    ) {
        Response response = blockServiceImpl.deleteBlock(details.getMember().getId(), blockId);
        return CommonResponse.status(HttpStatus.OK).body(response);
    }
}

