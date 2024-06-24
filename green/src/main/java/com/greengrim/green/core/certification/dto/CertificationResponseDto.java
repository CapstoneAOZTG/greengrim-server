package com.greengrim.green.core.certification.dto;

import static com.greengrim.green.common.entity.Time.calculateTime;

import com.greengrim.green.core.certification.entity.Certification;
import com.greengrim.green.core.certification.entity.VerificationFlag;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.ChallengeInfoForCertification;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.ChallengeTitleInfo;
import com.greengrim.green.core.member.dto.MemberResponseDto.MemberSimpleInfo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class CertificationResponseDto {

    @Getter
    @RequiredArgsConstructor
    public static class CertificationInfo {
        private Long id;
        private String title;
        private String description;
        private String imgUrl;
        private String createdAt;

        public CertificationInfo(Certification certification) {
            this.id = certification.getId();
            this.title = "[" + certification.getRound() + "회차 인증]";
            this.description = certification.getDescription();
            this.imgUrl = certification.getImgUrl();
            this.createdAt = calculateTime(certification.getCreatedAt(), 3);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class registerCertificationResponse {
        private Long certId;
        private String certImg;
        private String date;
        private boolean successChallenge;

        public registerCertificationResponse(Certification certification, boolean successChallenge) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일", Locale.KOREAN);
            this.certId = certification.getId();
            this.certImg = certification.getImgUrl();
            this.date = LocalDateTime.now().format(dateFormatter);
            this.successChallenge = successChallenge;
        }
    }


    @Getter
    @RequiredArgsConstructor
    public static class CertificationDetailInfo {
        ChallengeInfoForCertification challengeInfo;
        MemberSimpleInfo memberSimpleInfo;
        CertificationInfo certificationInfo;
        private VerificationFlag isVerified;
        private boolean isMine;

        public CertificationDetailInfo(Certification certification, VerificationFlag isVerified, boolean isMine) {
            this.memberSimpleInfo = new MemberSimpleInfo(certification.getMember());
            this.challengeInfo = new ChallengeInfoForCertification(certification.getChallenge());
            this.certificationInfo = new CertificationInfo(certification);
            this.isVerified = isVerified;
            this.isMine = isMine;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class CertificationsByMonth {
        List<String> date;
    }

    @Getter
    @AllArgsConstructor
    public static class CertificationsByChallengeDate {
        MemberSimpleInfo memberSimpleInfo;
        CertificationInfo certificationInfo;

        public CertificationsByChallengeDate(Certification certification) {
            this.memberSimpleInfo = new MemberSimpleInfo(certification.getMember());
            this.certificationInfo = new CertificationInfo(certification);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class CertificationsByMemberDate {
        ChallengeTitleInfo challengeTitleInfo;
        CertificationInfo certificationInfo;

        public CertificationsByMemberDate(Certification certification) {
            this.challengeTitleInfo = new ChallengeTitleInfo(certification.getChallenge());
            this.certificationInfo = new CertificationInfo(certification);
        }
    }

}
