package com.greengrim.green.core.member.service;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.MemberErrorCode;
import com.greengrim.green.common.jwt.JwtTokenProvider;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.member.Role;
import com.greengrim.green.core.member.dto.MemberRequestDto;
import com.greengrim.green.core.member.dto.MemberResponseDto;
import com.greengrim.green.core.member.repository.MemberRepository;
import com.greengrim.green.core.member.usecase.RegisterMemberUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterMemberService implements RegisterMemberUseCase {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void save(Member member) {
        memberRepository.save(member);
    }

    public Member register(MemberRequestDto.RegisterMemberReq registerMemberReq) {
        Member member = Member.builder()
                .email(registerMemberReq.getEmail())
                .nickName(registerMemberReq.getNickName())
                .introduction(registerMemberReq.getIntroduction())
                .profileImgUrl(registerMemberReq.getProfileImgUrl())
                .point(0)
                .reportCnt(0)
                .status(true)
                .refreshToken("")
                .deviceToken("")
                .role(Role.ROLE_VISITOR)
                .build();
        save(member);
        return member;
    }

    @Override
    public MemberResponseDto.TokenInfo registerMember(MemberRequestDto.RegisterMemberReq registerMemberReq) {
        checkRegister(registerMemberReq);
        Member member = register(registerMemberReq);
        MemberResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getId());
        member.changeRefreshToken(tokenInfo.getRefreshToken());
        return tokenInfo;
    }

    public void checkRegister(MemberRequestDto.RegisterMemberReq registerMemberReq) {
        Boolean flag = memberRepository.existsByEmail(registerMemberReq.getEmail());
        if (flag) {
            throw new BaseException(MemberErrorCode.DUPLICATE_MEMBER);
        }
        flag = memberRepository.existsByNickName(registerMemberReq.getNickName());
        if (flag) {
            throw new BaseException(MemberErrorCode.DUPLICATE_NICKNAME);
        }
    }

    @Override
    public MemberResponseDto.TokenInfo login(MemberRequestDto.LoginMemberReq loginMemberReq) {
        Member member = memberRepository.findByEmail(loginMemberReq.getEmail()).orElse(null);
        if (member != null) {
            MemberResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(member.getId());
            member.changeRefreshToken(tokenInfo.getRefreshToken());
            return tokenInfo;
        } else {
            throw new BaseException(MemberErrorCode.UN_REGISTERED_MEMBER);
        }
    }

    @Override
    public MemberResponseDto.CheckNickNameRes checkNickName(MemberRequestDto.CheckNickNameReq checkNickNameReq) {
        return new MemberResponseDto.CheckNickNameRes(
                memberRepository.existsByNickName(checkNickNameReq.getNickName()));
    }
}
