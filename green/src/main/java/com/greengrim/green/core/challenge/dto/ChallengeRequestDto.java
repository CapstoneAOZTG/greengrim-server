package com.greengrim.green.core.challenge.dto;

import com.greengrim.green.core.challenge.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public class ChallengeRequestDto {

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class RegisterChallenge {
        @NotNull
        private Category category;
        @NotNull
        @Size(min = 2, max = 50)
        private String title;           // 제목
        @Size(min = 2, max = 200)
        private String description;     // 설명
        private String imgUrl;          // 대표 사진
        @NotNull
        private int goalCount;          // 목표 인증 횟수
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRequest {
        @NotNull
        private String keyword;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyChallengesRequest {
        @NotNull
        private Long chatroomId;
        @NotNull
        private String lastVisit;
    }
}
