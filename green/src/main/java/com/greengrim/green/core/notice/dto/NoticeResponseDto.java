package com.greengrim.green.core.notice.dto;

import com.greengrim.green.core.notice.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class NoticeResponseDto {

    @Getter
    @AllArgsConstructor
    public static class NoticeSimpleInfo {
        private Long id;
        private String title;
        private String createdTime;

        public NoticeSimpleInfo (Notice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.createdTime = notice.getCreatedTime();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class NoticeDetailInfo {
        private String title;
        private String content;
        private String createdTime;

        public NoticeDetailInfo (Notice notice) {
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.createdTime = notice.getCreatedTime();
        }
    }
}
