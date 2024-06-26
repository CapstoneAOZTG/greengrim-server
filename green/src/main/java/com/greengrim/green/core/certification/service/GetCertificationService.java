package com.greengrim.green.core.certification.service;

import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.CertificationErrorCode;
import com.greengrim.green.common.exception.errorCode.VerificationErrorCode;
import com.greengrim.green.core.certification.entity.Certification;
import com.greengrim.green.core.certification.entity.VerificationFlag;
import com.greengrim.green.core.certification.dto.CertificationResponseDto.CertificationDetailInfo;
import com.greengrim.green.core.certification.dto.CertificationResponseDto.CertificationsByChallengeDate;
import com.greengrim.green.core.certification.dto.CertificationResponseDto.CertificationsByMemberDate;
import com.greengrim.green.core.certification.dto.CertificationResponseDto.CertificationsByMonth;
import com.greengrim.green.core.certification.repository.CertificationRepository;
import com.greengrim.green.core.challenge.entity.Challenge;
import com.greengrim.green.core.member.entity.Member;
import com.greengrim.green.core.verification.service.VerificationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCertificationService {

    private final VerificationService verificationService;
    private final CertificationRepository certificationRepository;

    /**
     * 인증 상세 보기
     */
    public CertificationDetailInfo getCertificationInfo(Member member, Long id) {
        Certification certification = certificationRepository.findById(id)
                .orElseThrow(() -> new BaseException(CertificationErrorCode.EMPTY_CERTIFICATION));

        VerificationFlag isVerified = checkVerificationFlag(member, certification);
        boolean isMine = checkIsMine(member.getId(), certification.getMember().getId());
        return new CertificationDetailInfo(certification, isVerified, isMine);
    }

    private VerificationFlag checkVerificationFlag(Member member, Certification certification) {
        // 상호 인증에 참여했는가 플래그 값 세팅
        if(member == null || certification.getValidation() != 0 || checkIsMine(member.getId(), certification.getMember().getId())) {
            return VerificationFlag.DEACTIVATION;
        }
        else if(verificationService.checkVerification(member.getId(), certification.getId())) {
            return VerificationFlag.TRUE;
        } else if(!verificationService.checkVerification(member.getId(), certification.getId())) {
            return VerificationFlag.FALSE;
        }
        return VerificationFlag.DEACTIVATION;
    }

    private boolean checkIsMine(Long memberId, Long ownerId) {
        return Objects.equals(memberId, ownerId);
    }

    /**
     * 멤버와 챌린지를 받아서 몇 회차 인증할 차례인지 반환
     */
    public int getRoundByMemberAndChallenge(Member member, Challenge challenge) {
        return certificationRepository.countsByMemberAndChallenge(member, challenge) + 1;
    }

    /**
     * 챌린지 월 별 인증 유무를 date 리스트 형식으로 반환
     */
    public CertificationsByMonth getCertificationsByChallengeMonth(Long challengeId) {
        List<String> date = certificationRepository.findCertificationsByChallengeMonth(challengeId);
        return new CertificationsByMonth(date);
    }

    /**
     * 멤버 월 별 인증 유무를 date 리스트 형식으로 반환
     */
    public CertificationsByMonth getCertificationsByMemberMonth(Long targetId, Member member) {
        if(targetId == null) {
            targetId = member.getId();
        }
        List<String> date = certificationRepository.findCertificationsByMemberMonth(targetId);
        return new CertificationsByMonth(date);
    }

    /**
     * 챌린지 날짜 별 인증 반환
     */
    public PageResponseDto<List<CertificationsByChallengeDate>> getCertificationsByChallengeDate(
            Member member, Long challengeId, String date, int page, int size) {
        List<CertificationsByChallengeDate> certificationsByChallengeDates = new ArrayList<>();

        Page<Certification> certifications = certificationRepository.findCertificationsByChallengeDate(
                member.getId(), date, challengeId, PageRequest.of(page, size));

        certifications.forEach(certification ->
                certificationsByChallengeDates.add(new CertificationsByChallengeDate(certification)));

        return  new PageResponseDto<>(certifications.getNumber(), certifications.hasNext(), certificationsByChallengeDates);
    }

    /**
     * 멤버 날짜 별 인증 반환
     */
    public PageResponseDto<List<CertificationsByMemberDate>> getCertificationsByMemberDate(
            Long targetId, Member member, String date, int page, int size) {
        if(targetId == null) {
            targetId = member.getId();
        }
        List<CertificationsByMemberDate> certificationsByMemberDates = new ArrayList<>();

        Page<Certification> certifications = certificationRepository.findCertificationsByMemberDate(
                member.getId(), date, targetId, PageRequest.of(page, size));

        certifications.forEach(certification ->
                certificationsByMemberDates.add(new CertificationsByMemberDate(certification)));

        return  new PageResponseDto<>(certifications.getNumber(), certifications.hasNext(), certificationsByMemberDates);
    }

    /**
     * 출석체크 할 인증의 id를 반환
     * 조건 1. certification.validation = 0
     * 조건 2. certification.verificationCount 가 작은 순
     * 조건 3. 이미 참여했거나 내 인증은 제외
     * 조건 4. 차단한 Member, 차단한 Challenge, 차단한 Certification 제외
     */
    public Long findCertificationForVerification(Member member) {
        return certificationRepository.findCertificationForVerification(member.getId())
                .orElseThrow(() -> new BaseException(VerificationErrorCode.NOT_EXIST_VERIFICATION));
    }
}
