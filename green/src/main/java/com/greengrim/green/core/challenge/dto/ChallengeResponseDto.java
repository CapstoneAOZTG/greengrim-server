package com.greengrim.green.core.challenge.dto;

import static com.greengrim.green.common.entity.Time.calculateTime;

import com.greengrim.green.core.challenge.Category;
import com.greengrim.green.core.challenge.Challenge;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ChallengeResponseDto {

    @Getter
    @RequiredArgsConstructor
    public static class ChallengeInfo {
        private Long id;
        private String title;
        private String description;
        private String imgUrl;

        public ChallengeInfo(Challenge challenge) {
            this.id = challenge.getId();
            this.title = challenge.getTitle();
            this.description = challenge.getDescription();
            this.imgUrl = challenge.getImgUrl();
        }

        public ChallengeInfo(Challenge challenge, String description) {
            this.id = challenge.getId();
            this.title = challenge.getTitle();
            this.description = description;
            this.imgUrl = challenge.getImgUrl();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ChallengeTags {
        private Category category;
        private String ticketCount;
        private String goalCount;
        private String weekMinCount;
        private String participantCount;

        public ChallengeTags(Challenge challenge) {
            this.category = challenge.getCategory();
            this.goalCount = challenge.getGoalCountTag();
            this.ticketCount = challenge.getTicketCountTag();
            this.weekMinCount = challenge.getWeekMinCountTag();
            this.participantCount = challenge.getParticipantCountTag();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ChallengeSimpleTags {
        private Category category;
        private String ticketCount;
        private String goalCount;

        public ChallengeSimpleTags(Challenge challenge) {
            this.category = challenge.getCategory();
            this.goalCount = challenge.getGoalCountTag();
            this.ticketCount = challenge.getTicketCountTag();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ChallengeDetailInfo {
        private ChallengeInfo challengeInfo;
        private ChallengeTags challengeTags;
        private String createdAt;
        private boolean isEntered;
        private boolean isMine;

        public ChallengeDetailInfo(Challenge challenge, boolean isEntered, boolean isMine) {
            this.challengeInfo = new ChallengeInfo(challenge);
            this.challengeTags = new ChallengeTags(challenge);
            this.createdAt = calculateTime(challenge.getCreatedAt(), 1);
            this.isEntered = isEntered;
            this.isMine = isMine;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ChallengeSimpleInfo {
        private ChallengeInfo challengeInfo;
        private ChallengeSimpleTags challengeSimpleTags;

        public ChallengeSimpleInfo(Challenge challenge) {
            this.challengeInfo = new ChallengeInfo(challenge);
            this.challengeSimpleTags = new ChallengeSimpleTags(challenge);
        }
    }

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class HomeChallenges {
        private List<ChallengeInfo> challengeInfos;
    }

    @Getter
    @RequiredArgsConstructor
    public static class ChallengeInfoForCertification {
        private Long id;
        private String title;
        private String description;
        private Category category;
        private String ticketCount;

        public ChallengeInfoForCertification(Challenge challenge) {
            this.id = challenge.getId();
            this.title = challenge.getTitle();
            this.description = challenge.getDescription();
            this.category = challenge.getCategory();
            this.ticketCount = challenge.getTicketCountTag();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ChallengeTitleInfo {
        private Long id;
        private String title;
        private String imgUrl;
        private Category category;

        public ChallengeTitleInfo(Challenge challenge) {
            this.id = challenge.getId();
            this.title = challenge.getTitle();
            this.imgUrl = challenge.getImgUrl();
            this.category = challenge.getCategory();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class MyChatroom {
        private Long challengeId;
        private Long chatroomId;
        private String title;
        private String ImgUrl;
        private String afterDay;

        public MyChatroom(Challenge challenge, String afterDay) {
            this.challengeId = challenge.getId();
            this.chatroomId = challenge.getChatroom().getId();
            this.title = challenge.getTitle();
            this.ImgUrl = challenge.getImgUrl();
            this.afterDay = afterDay;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class EnterChallengeResponse {

        private Long challengeId;
        private Long chatroomId;
        private String title;

        public EnterChallengeResponse(Challenge challenge) {
            this.challengeId = challenge.getId();
            this.chatroomId = challenge.getChatroom().getId();
            this.title = challenge.getTitle();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ChatroomTopBarInfo {
        private Category category;
        private String ticketCount;
        private String goalCount;
        private int certificationCount;
        private boolean todayCertification; // 오늘 인증했는지 여부

        public ChatroomTopBarInfo(Challenge challenge, int certificationCount, boolean todayCertification) {
            this.category = challenge.getCategory();
            this.ticketCount = challenge.getTicketCountTag();
            this.goalCount = "인증 " + certificationCount + "/" + challenge.getGoalCount() + "회";
            this.certificationCount = certificationCount;
            this.todayCertification = todayCertification;
        }
    }
}
