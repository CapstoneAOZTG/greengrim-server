package com.greengrim.green.core.event.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class EventResponseDto {

    @Getter
    @RequiredArgsConstructor
    public static class EventInfo {
        private String title;
        private String imgUrl;

        public EventInfo(String title, String imgUrl) {
            this.title = title;
            this.imgUrl = imgUrl;
        }
    }
}
