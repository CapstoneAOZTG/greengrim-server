package com.greengrim.green.core.member.dto;

import static com.greengrim.green.common.util.UtilService.formatIntToString;

import com.greengrim.green.core.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberResponseDto {

    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        private String accessToken;
        private String refreshToken;
        private Long memberId;
    }

    @Getter
    @AllArgsConstructor
    public static class CheckNickNameRes {
        private boolean isUsed;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberInfo {
        private Long id;
        private String nickName;
        private String introduction;
        private String profileImgUrl;
        private boolean isMine;

        public MemberInfo(Member member, boolean isMine) {
            this.id = member.getId();
            this.nickName = member.getNickName();
            this.introduction = member.getIntroduction();
            this.profileImgUrl = member.getProfileImgUrl();
            this.isMine = isMine;
        }
    }
    
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberSimpleInfo {
        private Long id;
        private String nickName;
        private String profileImgUrl;

        public MemberSimpleInfo(Member member) {
            this.id = member.getId();
            this.nickName = member.getNickName();
            this.profileImgUrl = member.getProfileImgUrl();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class HomeInfo {
        private String nickName;
        private String carbonReduction;
        private String point;

        public HomeInfo(Member member) {
            this.nickName = member.getNickName();
            this.carbonReduction = member.getCarbonReduction() + " g";
            this.point = formatIntToString(member.getPoint()) + " GP";
        }
    }

    @Getter
    @AllArgsConstructor
    public static class MyInfo {
        private MemberInfo memberInfo;
        private String email;
        private int point;

        public MyInfo(Member member) {
            this.memberInfo = new MemberInfo(member, true);
            this.email = member.getEmail();
            this.point = member.getPoint();
        }
    }
}
