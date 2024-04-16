package com.greengrim.green.core.certification;

import com.greengrim.green.common.entity.BaseTime;
import com.greengrim.green.core.challenge.Challenge;
import com.greengrim.green.core.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Certification extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String imgUrl;
    @Size(max = 200)
    private String description;

    @NotNull
    private int round;                  // 인증 회차
    @NotNull
    @Max(2)
    private int validation;             // 상호 인증 상태: 0=진행중, 1=성공, 2=실패
    @NotNull
    @Max(10)
    private int verificationCount;      // 남은 상호 인증 횟수

    @NotNull
    private int reportCount;

    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge challenge;


    public void setValidationSuccess() {
        this.validation = 1;
    }

    public void setValidationFail() {
        this.validation = 2;
        this.status = false;
    }

    public void minusVerificationCount() {
        this.verificationCount--;
    }

    public void plusReportCount() {
        this.reportCount++;
    }

    public void setStatusFalse() {
        this.status = false;
    }
}
