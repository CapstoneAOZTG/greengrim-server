package com.greengrim.green.core.member.service;

import com.greengrim.green.common.fcm.FcmService;
import com.greengrim.green.common.fcm.FcmTopicType;
import com.greengrim.green.common.oauth.jwt.JwtTokenProvider;
import com.greengrim.green.common.s3.S3Service;
import com.greengrim.green.core.certification.service.UpdateCertificationService;
import com.greengrim.green.core.chat.service.ChatService;
import com.greengrim.green.core.member.entity.Member;
import com.greengrim.green.core.member.entity.MemberAlarm;
import com.greengrim.green.core.member.dto.MemberRequestDto.ModifyProfile;
import com.greengrim.green.core.member.dto.MemberResponseDto;
import com.greengrim.green.core.member.repository.MemberRepository;
import com.greengrim.green.core.nft.service.UpdateNftService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateMemberService  {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    private final UpdateCertificationService updateCertificationService;
    private final UpdateNftService updateNftService;
    private final ChatService chatService;
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
        // 프로필이 존재하면 s3에서 기존 프로필 삭제
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
        // 인증 soft delete
        updateCertificationService.setCertificationStatusFalseByMember(member);
        // NFT soft delete
        updateNftService.setCertificationStatusFalseByMember(member);
        // 멤버 soft delete
        member.setStatusFalse();
    }

    public void updateAlarmStatus(Member member, MemberAlarm memberAlarm) {
        if(memberAlarm.equals(MemberAlarm.PUSH)) updateIsPushAlarmOn(member);
        else if(memberAlarm.equals(MemberAlarm.CHAT)) updateIsChatAlarmOn(member);
        else if(memberAlarm.equals(MemberAlarm.ISSUE)) updateIsIssueAlarmOn(member);
        else if(memberAlarm.equals(MemberAlarm.NOTICE)) updateIsNoticeAlarmOn(member);
    }

    public void updateIsPushAlarmOn(Member member) {
        if(member.isPushAlarmOn()) member.setIsPushAlarmOn(false);
        else member.setIsPushAlarmOn(true);
    }

    public void updateIsChatAlarmOn(Member member) {
        if(member.isChatAlarmOn()) member.setIsChatAlarmOn(false);
        else member.setIsChatAlarmOn(true);
    }

    public void updateIsIssueAlarmOn(Member member) {
        if(member.isIssueAlarmOn()) {
            member.setIsIssueAlarmOn(false);
            fcmService.unsubscribeTopic(member, FcmTopicType.TOPIC_ISSUE);
        }
        else {
            member.setIsPushAlarmOn(true);
            fcmService.subscribeTopic(member, FcmTopicType.TOPIC_ISSUE);
        }
    }

    public void updateIsNoticeAlarmOn(Member member) {
        if(member.isNoticeAlarmOn()) {
            member.setIsNoticeAlarmOn(false);
            fcmService.unsubscribeTopic(member, FcmTopicType.TOPIC_NOTICE);
        }
        else{
            member.setIsNoticeAlarmOn(true);
            fcmService.subscribeTopic(member, FcmTopicType.TOPIC_NOTICE);
        }
    }
}