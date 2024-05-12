package com.greengrim.green.core.issue.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class IssueRequestDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IssueRequest {
        @NotBlank(message = "title을 입력해주세요")
        private String title;
        @NotBlank(message = "imgUrl을 입력해주세요")
        private String imgUrl;
        @NotBlank(message = "url을 입력해주세요")
        private String url;
    }
}
