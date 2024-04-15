package com.greengrim.green.core.member.service;

import com.greengrim.green.common.fcm.FcmService;
import com.greengrim.green.common.oauth.jwt.JwtTokenProvider;
import com.greengrim.green.common.s3.S3Service;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.member.dto.MemberRequestDto.ModifyProfile;
import com.greengrim.green.core.member.dto.MemberResponseDto;
import com.greengrim.green.core.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateMemberService  {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    private final FcmService fcmService;
    private final S3Service s3Service;

    public MemberResponseDto.TokenInfo refreshAccessToken(Member member) {
        MemberResponseDto.TokenInfo newTokenInfo
                = jwtTokenProvider.generateToken(member.getId());

        // DB refreshToken 변경
        member.changeRefreshToken(newTokenInfo.getRefreshToken());
        memberRepository.save(member);

        return new MemberResponseDto.TokenInfo(
                newTokenInfo.getAccessToken(), newTokenInfo.getRefreshToken(), member.getId());
    }

    public void modifyProfile(Member member, ModifyProfile modifyProfile) {
        // 프로필이 존재한다면 s3에서 기존 프로필 삭제
        if(member.existProfileImgUrl()) {
            s3Service.deleteFile(member.getProfileImgUrl());
        }
        // Member 엔티티 업로드
        member.modifyMember(modifyProfile.getNickName(),
                modifyProfile.getIntroduction(),
                modifyProfile.getProfileImgUrl());
        memberRepository.save(member);
    }

    public void deleteMember(Member member) {
        // member.setStatusFalse();
        // TODO: 삭제된 member와 관련된 모든 리소스 삭제
        memberRepository.delete(member);
    }

    public void plusPoint(Member member) {
        member.plusPoint(10);
        fcmService.sendGetPoint(member, "미니게임", 10);
        memberRepository.save(member);
    }
}
