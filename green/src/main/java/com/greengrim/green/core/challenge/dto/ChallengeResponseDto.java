package com.greengrim.green.core.challenge.dto;

import static com.greengrim.green.common.entity.Time.calculateTime;

import com.greengrim.green.core.challenge.entity.Category;
import com.greengrim.green.core.challenge.entity.Challenge;
import com.greengrim.green.core.chat.entity.ChatMessage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
        private String goalCount;
        private String participantCount;

        public ChallengeTags(Challenge challenge) {
            this.category = challenge.getCategory();
            this.goalCount = challenge.getGoalCountTag();
            this.participantCount = challenge.getParticipantCountTag();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ChallengeDetailInfo {
        private ChallengeInfo challengeInfo;
        private ChallengeTags challengeTags;
        private Long chatroomId;
        private String createdAt;
        private boolean isEntered;
        private boolean isMine;


        public ChallengeDetailInfo(Challenge challenge, boolean isEntered, boolean isMine) {
            this.challengeInfo = new ChallengeInfo(challenge);
            this.challengeTags = new ChallengeTags(challenge);
            this.createdAt = calculateTime(challenge.getCreatedAt(), 1);
            this.isEntered = isEntered;
            this.chatroomId = challenge.getChatroom().getId();
            this.isMine = isMine;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ChallengeSimpleInfo {
        private ChallengeInfo challengeInfo;
        private ChallengeTags challengeTags;

        public ChallengeSimpleInfo(Challenge challenge) {
            this.challengeInfo = new ChallengeInfo(challenge);
            this.challengeTags = new ChallengeTags(challenge);
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

        public ChallengeInfoForCertification(Challenge challenge) {
            this.id = challenge.getId();
            this.title = challenge.getTitle();
            this.description = challenge.getDescription();
            this.category = challenge.getCategory();
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
    public static class MyChallengeInfo {
        private Long id;
        private String title;
        private String imgUrl;
        private ChatroomInfo chatroomInfo;

        public MyChallengeInfo(Challenge challenge, ChatroomInfo chatroomInfo) {
            this.id = challenge.getId();
            this.title = challenge.getTitle();
            this.imgUrl = challenge.getImgUrl();
            this.chatroomInfo = chatroomInfo;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ChatroomInfo {
        private Long chatroomId;
        private String lastMessageContent;
        private String lastMessageTime;
        private Long newMessageCount;
        private String createdAt;

        public ChatroomInfo(Long chatroomId, ChatMessage chatMessage, Long newMessageCount) {
            this.chatroomId = chatroomId;
            this.lastMessageContent = chatMessage.getMessage();
            this.lastMessageTime = calLastMessageTime(chatMessage);
            this.newMessageCount = newMessageCount;
            this.createdAt = chatMessage.getCreatedAt();
        }

        public ChatroomInfo(Long chatroomId) {
            this.chatroomId = chatroomId;
            this.lastMessageContent = "";
            this.lastMessageTime = "";
            this.newMessageCount = 0L;
            this.createdAt = "0";
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class EnterChallengeInfo {

        private Long challengeId;
        private Long chatroomId;
        private String title;
        private String imgUrl;

        public EnterChallengeInfo(Challenge challenge) {
            this.challengeId = challenge.getId();
            this.chatroomId = challenge.getChatroom().getId();
            this.title = challenge.getTitle();
            this.imgUrl = challenge.getImgUrl();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ChatroomTopBarInfo {
        private Category category;
        private String participantCount;
        private String goalCount;
        private int certificationCount;
        private boolean todayCertification; // 오늘 인증했는지 여부

        public ChatroomTopBarInfo(Challenge challenge, int certificationCount, boolean todayCertification) {
            this.category = challenge.getCategory();
            this.participantCount = challenge.getParticipantCountTag();
            this.goalCount = "인증 " + certificationCount + "/" + challenge.getGoalCount() + "회";
            this.certificationCount = certificationCount + 1;
            this.todayCertification = todayCertification;
        }
    }

    private static String calLastMessageTime(ChatMessage chatMessage) {
        String createAtStr = chatMessage.getCreatedAt();

        LocalDateTime createAt = LocalDateTime.parse(createAtStr, DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        LocalDate today = LocalDate.now();

        long daysAgo = ChronoUnit.DAYS.between(createAt.toLocalDate(), today);

        if (daysAgo == 0) return chatMessage.getSentTime();
        return daysAgo + "일 전";
    }
}
