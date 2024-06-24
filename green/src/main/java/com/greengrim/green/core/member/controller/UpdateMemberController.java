package com.greengrim.green.core.member.controller;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.member.MemberAlarm;
import com.greengrim.green.core.member.dto.MemberRequestDto.ModifyProfile;
import com.greengrim.green.core.member.dto.MemberResponseDto;
import com.greengrim.green.core.member.service.UpdateMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor/members")
@Tag(name = "멤버")
public class UpdateMemberController {

    private final UpdateMemberService updateMemberService;

    /**
     * [PATCH] 로그인 토큰 갱신
     */
    @Operation(summary = "로그인 토큰 갱신")
    @PatchMapping("/refresh")
    public ResponseEntity<MemberResponseDto.TokenInfo> refreshLogin(
            @CurrentMember Member member) {
        return new ResponseEntity<>(updateMemberService.refreshAccessToken(member),
                HttpStatus.OK);
    }

    /**
     * [PATCH] 프로필 수정
     */
    @Operation(summary = "프로필 수정")
    @PatchMapping("/profile")
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
    @DeleteMapping("/delete")
    public ResponseEntity<Integer> deleteMember(
            @CurrentMember Member member) {
        updateMemberService.deleteMember(member);
        return new ResponseEntity<>(200, HttpStatus.OK);
    }

    /**
     * [PATCH] 알람 설정 변경
     */
    @Operation(summary = "알람 설정 변경")
    @PatchMapping("/alarm")
    public void updateAlarmStatus(
        @CurrentMember Member member,
        @RequestBody MemberAlarm memberAlarm) {
        updateMemberService.updateAlarmStatus(member, memberAlarm);
    }

}
