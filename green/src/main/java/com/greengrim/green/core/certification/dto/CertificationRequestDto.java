package com.greengrim.green.core.certification.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class CertificationRequestDto {

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class RegisterCertification {
        @NotNull(message = "챌린지 id를 입력해주세요.")
        private Long challengeId;
        @NotNull(message = "인증 이미지를 첨부해주세요.")
        private String imgUrl;
        @Size(max = 200, message = "설명은 200자 이내여야합니다.")
        private String description;
    }
}
