package com.greengrim.green.core.member.dto;

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
    }

    @Getter
    @AllArgsConstructor
    public static class CheckNickNameRes {
        private boolean isUsed;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberSimpleInfo {
        private Long id;
        private String nickName;
        private String introduction;
        private String profileImgUrl;

        public MemberSimpleInfo(Member member) {
            this.id = member.getId();
            this.nickName = member.getNickName();
            this.introduction = member.getIntroduction();
            this.profileImgUrl = member.getProfileImgUrl();
        }
    }
}
