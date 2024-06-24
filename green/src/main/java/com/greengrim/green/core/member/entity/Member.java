package com.greengrim.green.core.member.entity;

import com.greengrim.green.common.entity.BaseTime;
import com.greengrim.green.core.wallet.entity.Wallet;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static com.greengrim.green.common.constants.ServerConstants.BASIC_PROFILE_IMG_URL;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String nickName;

    @NotNull
    @Builder.Default
    private int point = 0;

    @NotNull
    @Builder.Default
    private double carbonReduction = 0.0;

    private String introduction;

    private String profileImgUrl;

    @NotNull
    @Builder.Default
    private boolean status = true;

    @NotNull
    @Builder.Default
    private Integer reportCount = 0;

    @NotNull
    @Builder.Default
    private String refreshToken = "";

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    private Wallet wallet;

    @NotNull
    private String fcmToken;

    @NotNull
    private boolean isPushAlarmOn;

    @NotNull
    private boolean isChatAlarmOn;

    @NotNull
    private boolean isIssueAlarmOn;

    @NotNull
    private boolean isNoticeAlarmOn;

    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public void changeFcmToken(String fcmToken) { this.fcmToken = fcmToken; }

    public void modifyMember(String nickName,
                             String introduction,
                             String profileImgUrl) {
        this.nickName = nickName;
        this.introduction = introduction;
        this.profileImgUrl = profileImgUrl;
    }

    public void setStatusFalse() {
        this.status = false;
    }

    public boolean existProfileImgUrl() {
        return this.profileImgUrl != null && !this.profileImgUrl.equals("");
    }

    public String getProfileImgUrl() {
        if(!existProfileImgUrl()) {
            return BASIC_PROFILE_IMG_URL;
        }
        return this.profileImgUrl;
    }

    public void plusPoint(int point) {
        this.point += point;
    }

    public void minusPoint(int point) {
        this.point -= point;
        if(this.point < 0) {
            this.point = 0;
        }
    }

    public void setCarbonReduction(double carbonReduction) {
        this.carbonReduction += carbonReduction;
    }

    public void changeWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public void changeRole(Role role) {
        this.role = role;
    }

    public void plusReportCount() {
        this.reportCount++;
    }

    public void setIsPushAlarmOn(boolean isPushAlarmOn) { this.isPushAlarmOn = isPushAlarmOn; }
    public void setIsChatAlarmOn(boolean isChatAlarmOn) { this.isChatAlarmOn = isChatAlarmOn; }
    public void setIsIssueAlarmOn(boolean isIssueAlarmOn) { this.isIssueAlarmOn = isIssueAlarmOn; }
    public void setIsNoticeAlarmOn(boolean isNoticeAlarmOn) { this.isNoticeAlarmOn = isNoticeAlarmOn; }

}
