package com.greengrim.green.core.member.controller;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.member.dto.MemberRequestDto.ModifyProfile;
import com.greengrim.green.core.member.dto.MemberResponseDto;
import com.greengrim.green.core.member.service.UpdateMemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateMemberController {

    private final UpdateMemberService updateMemberService;

    /**
     * [PATCH] 로그인 토큰 갱신
     */
    @Operation(summary = "로그인 토큰 갱신")
    @PatchMapping("/visitor/refresh")
    public ResponseEntity<MemberResponseDto.TokenInfo> refreshLogin(
            @CurrentMember Member member) {
        return new ResponseEntity<>(updateMemberService.refreshAccessToken(member),
                HttpStatus.OK);
    }

    /**
     * [PATCH] 프로필 수정
     */
    @Operation(summary = "프로필 수정")
    @PatchMapping("/visitor/profile")
    public ResponseEntity<Integer> modifyProfile(
            @CurrentMember Member member,
            @RequestBody ModifyProfile modifyProfile) {
        updateMemberService.modifyProfile(member, modifyProfile);
        return new ResponseEntity<>(200, HttpStatus.OK);
    }

    /**
     * [DELETE] 회원 탈퇴
     */
    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/visitor/delete")
    public ResponseEntity<Integer> deleteMember(
            @CurrentMember Member member) {
        updateMemberService.deleteMember(member);
        return new ResponseEntity<>(200, HttpStatus.OK);
    }

    /**
     * [POST] 포인트 획득
     */
    @Operation(summary = "포인트 획득")
    @PostMapping("/visitor/point")
    public void plusPoint(
        @CurrentMember Member member) {
        updateMemberService.plusPoint(member);
    }
}
