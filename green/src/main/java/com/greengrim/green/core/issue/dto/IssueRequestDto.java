package com.greengrim.green.core.issue.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class IssueRequestDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IssueRequest {
        @NotBlank(message = "title을 입력해주세요")
        private String title;
        @NotBlank(message = "iconImgUrl을 입력해주세요")
        private String iconImgUrl;
        @NotBlank(message = "content를 입력해주세요")
        private String content;

        private List<String> imgUrls;
    }
}
