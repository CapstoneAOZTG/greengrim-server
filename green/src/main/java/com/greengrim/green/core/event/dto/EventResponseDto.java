package com.greengrim.green.core.event.dto;

import com.greengrim.green.core.event.entity.Event;
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
        private Long resourceId;

        public EventInfo(Event event) {
            this.title = event.getTitle();
            this.imgUrl = event.getImgUrl();
            this.isWebView = event.isWebView();
            this.url = event.getUrl();
            this.resourceId = event.getResourceId();
        }
    }
}
