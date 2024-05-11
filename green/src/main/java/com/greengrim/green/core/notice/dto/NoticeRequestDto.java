package com.greengrim.green.core.notice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class NoticeRequestDto {

    @Getter
    @AllArgsConstructor
    public static class RegisterNotice {
        @NotNull
        @Size(min = 2, max = 50)
        private String title;
        @NotNull
        @Size(min = 2, max = 200)
        private String content;
    }

}
