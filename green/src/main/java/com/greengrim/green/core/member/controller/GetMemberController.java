package com.greengrim.green.core.member.controller;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.member.dto.MemberResponseDto.HomeInfo;
import com.greengrim.green.core.member.dto.MemberResponseDto.MemberInfo;
import com.greengrim.green.core.member.dto.MemberResponseDto.MyInfo;
import com.greengrim.green.core.member.service.GetMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor/members")
@Tag(name = "멤버")
public class GetMemberController {

    private final GetMemberService getMemberService;

    /**
     * [GET] 프로필 조회
     */
    @Operation(summary = "프로필 조회",
            description = "자신의 것을 조회하고 싶다면 memberId는 안 보내셔도 됩니다!")
    @GetMapping("/profile")
    public ResponseEntity<MemberInfo> getCurrentMemberInfo(
            @CurrentMember Member member,
            @RequestParam(value = "memberId", required = false) Long memberId) {
        return new ResponseEntity<>(getMemberService.getMemberInfo(member, memberId),
                HttpStatus.OK);
    }

    /**
     * [GET] 홈 화면 내 정보 조회
     */
    @Operation(summary = "홈 화면 내 정보 조회")
    @GetMapping("/home")
    public ResponseEntity<HomeInfo> getHomeInfo(
            @CurrentMember Member member) {
        return new ResponseEntity<>(getMemberService.getHomeInfo(member),
                HttpStatus.OK);
    }

    /**
     * [GET] 더보기 조회
     */
    @Operation(summary = "더보기 조회")
    @GetMapping("/my")
    public ResponseEntity<MyInfo> getMyDetailInfo(
            @CurrentMember Member member) {
        return new ResponseEntity<>(getMemberService.getMyDetailInfo(member),
                HttpStatus.OK);
    }
}
