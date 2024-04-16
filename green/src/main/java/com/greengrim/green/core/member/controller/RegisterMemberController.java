package com.greengrim.green.core.member.controller;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.member.dto.MemberRequestDto;
import com.greengrim.green.core.member.dto.MemberResponseDto;
import com.greengrim.green.core.member.service.RegisterMemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterMemberController {

    private final RegisterMemberService registerMemberService;

    /**
     * [POST] 소셜 회원가입
     * /sign-up
     */
    @Operation(summary = "소셜 회원가입")
    @PostMapping("/sign-up")
    public ResponseEntity<MemberResponseDto.TokenInfo> register(
            @Valid @RequestBody MemberRequestDto.RegisterMemberReq registerMemberReq) {
        return new ResponseEntity<>(registerMemberService.registerMember(registerMemberReq),
                HttpStatus.OK);
    }

    /**
     * [POST] 로그인
     * /login
     */
    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<MemberResponseDto.TokenInfo> login(
            @Valid @RequestBody MemberRequestDto.LoginMemberReq loginMemberReq) {
        return new ResponseEntity<>(registerMemberService.login(loginMemberReq),
                HttpStatus.OK);
    }

    /**
     * [POST] 닉네임 중복 확인
     * /nick-name
     */
    @Operation(summary = "닉네임 중복 확인")
    @PostMapping("/nick-name")
    public ResponseEntity<MemberResponseDto.CheckNickNameRes> checkNickName(
            @Valid @RequestBody MemberRequestDto.CheckNickNameReq checkNickNameReq) {
        return new ResponseEntity<>(registerMemberService.checkNickName(checkNickNameReq),
                HttpStatus.OK);
    }

    /**
     * [POST] 로그아웃
     * /visitor/logout
     */
    @Operation(summary = "로그아웃")
    @PostMapping("/visitor/logout")
    public ResponseEntity<Integer> logout(
            @CurrentMember Member member) {
        registerMemberService.logout(member);
        return new ResponseEntity<>(200, HttpStatus.OK);
    }

}
