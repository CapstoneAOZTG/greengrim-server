package com.greengrim.green.core.member;

import com.greengrim.green.common.entity.BaseTime;
import com.greengrim.green.core.wallet.Wallet;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    private int point;

    @NotNull
    private double carbonReduction;

    private String introduction;

    private String profileImgUrl;

    @NotNull
    private boolean status;

    @NotNull
    private Integer reportCount;

    @NotNull
    private String refreshToken;

    @NotNull
    private String deviceToken;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    private Wallet wallet;

    @NotNull
    private String fcmToken;

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

}
