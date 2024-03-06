package com.greengrim.green.core.challenge.dto;

import com.greengrim.green.core.challenge.Category;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
        @NotNull
        private int ticketTotalCount;   // 총 티켓 개수
        @NotNull
        @Min(2) @Max(6)
        private int weekMinCount;       // 주 최소 인증 횟수
        @NotNull
        @Max(100)
        private int capacity;           // 수용 가능 인원
    }
}
