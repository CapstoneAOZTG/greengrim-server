package com.greengrim.green.core.report.service;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.CertificationErrorCode;
import com.greengrim.green.common.exception.errorCode.ChallengeErrorCode;
import com.greengrim.green.common.exception.errorCode.MemberErrorCode;
import com.greengrim.green.common.exception.errorCode.NftErrorCode;
import com.greengrim.green.core.certification.Certification;
import com.greengrim.green.core.certification.repository.CertificationRepository;
import com.greengrim.green.core.challenge.Challenge;
import com.greengrim.green.core.challenge.repository.ChallengeRepository;
import com.greengrim.green.core.hiding.certification.CertificationHidingService;
import com.greengrim.green.core.hiding.challenge.ChallengeHidingService;
import com.greengrim.green.core.hiding.member.MemberHidingService;
import com.greengrim.green.core.hiding.nft.NftHidingService;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.member.repository.MemberRepository;
import com.greengrim.green.core.nft.Nft;
import com.greengrim.green.core.nft.repository.NftRepository;
import com.greengrim.green.core.report.Report;
import com.greengrim.green.core.report.ReportType;
import com.greengrim.green.core.report.dto.ReportRequestDto.RegisterReport;
import com.greengrim.green.core.report.repository.ReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;
    private final CertificationRepository certificationRepository;
    private final NftRepository nftRepository;

    private final MemberHidingService memberHidingService;
    private final ChallengeHidingService challengeHidingService;
    private final CertificationHidingService certificationHidingService;
    private final NftHidingService nftHidingService;

    private static final int REPORT_COUNT_STANDARD = 10;

    public void register(Member member, ReportType type, RegisterReport registerReport) {
        Report report = Report.builder()
                .type(type)
                .resourceId(registerReport.getResourceId())
                .reportReason(registerReport.getReason())
                .content(registerReport.getContent())
                .member(member)
                .build();
        reportRepository.save(report);
    }

    public void registerReport(Member member, ReportType type, RegisterReport registerReport) {
        switch (type) {
            case MEMBER:
                reportMember(member, registerReport);
            case CHALLENGE:
                reportChallenge(member, registerReport);
            case CERTIFICATION:
                reportCertification(member, registerReport);
            case NFT:
                reportNft(member, registerReport);
        }
    }

    public void reportMember(Member member, RegisterReport registerReport) {
        Member reportedMember = memberRepository.findByIdAndStatusTrue(registerReport.getResourceId())
                .orElseThrow(() -> new BaseException(MemberErrorCode.EMPTY_MEMBER));
        register(member, ReportType.MEMBER, registerReport);
        reportedMember.plusReportCount();

        // 신고한 멤버에게 더이상 노출되지 않도록 차단 처리
        memberHidingService.hideMember(member, reportedMember.getId());
        // 누적 신고 횟수가 기준치 이상이라면 임시 삭제 처리
        // TODO: 이메일 전송
        if (reportedMember.getReportCount() >= REPORT_COUNT_STANDARD) {
            reportedMember.setStatusFalse();
        }
    }

    public void reportChallenge(Member member, RegisterReport registerReport) {
        Challenge reportedChallenge = challengeRepository.findByIdAndStatusIsTrue(registerReport.getResourceId())
                .orElseThrow(() -> new BaseException(ChallengeErrorCode.EMPTY_CHALLENGE));
        register(member, ReportType.CHALLENGE, registerReport);
        reportedChallenge.plusReportCount();

        // 신고한 멤버에게 더이상 노출되지 않도록 차단 처리
        challengeHidingService.hideChallenge(member, reportedChallenge.getId());
        // 누적 신고 횟수가 기준치 이상이라면 임시 삭제 처리
        if (reportedChallenge.getReportCount() >= REPORT_COUNT_STANDARD) {
            reportedChallenge.setStatusFalse();
        }
    }

    public void reportCertification(Member member, RegisterReport registerReport) {
        Certification reportedCertification = certificationRepository.findByIdAndStatusIsTrue(
                        registerReport.getResourceId())
                .orElseThrow(() -> new BaseException(CertificationErrorCode.EMPTY_CERTIFICATION));
        register(member, ReportType.CERTIFICATION, registerReport);
        reportedCertification.plusReportCount();

        // 신고한 멤버에게 더이상 노출되지 않도록 차단 처리
        certificationHidingService.hideCertification(member, reportedCertification.getId());
        // 누적 신고 횟수가 기준치 이상이라면 임시 삭제 처리
        if (reportedCertification.getReportCount() >= REPORT_COUNT_STANDARD) {
            reportedCertification.setStatusFalse();
        }

    }

    public void reportNft(Member member, RegisterReport registerReport) {
        Nft reportedNft = nftRepository.findByIdAndStatusTrue(registerReport.getResourceId())
                .orElseThrow(() -> new BaseException(NftErrorCode.EMPTY_NFT));
        register(member, ReportType.NFT, registerReport);
        reportedNft.plusReportCount();

        // 신고한 멤버에게 더이상 노출되지 않도록 차단 처리
        nftHidingService.hideNft(member, reportedNft.getId());

        // 누적 신고 횟수가 기준치 이상이라면 임시 삭제 처리
        if (reportedNft.getReportCount() >= REPORT_COUNT_STANDARD) {
            reportedNft.setStatusFalse();
        }
    }


}
