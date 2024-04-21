package com.greengrim.green.core.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EventResponseDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventInfo {
        private String title;
        private String imgUrl;
        private boolean isWebView;
        private String url;
    }
}
