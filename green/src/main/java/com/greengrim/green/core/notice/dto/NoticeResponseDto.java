package com.greengrim.green.core.notice.dto;

import com.greengrim.green.common.entity.Time;
import com.greengrim.green.core.notice.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class NoticeResponseDto {

    @Getter
    @AllArgsConstructor
    public static class NoticeSimpleInfo {
        private Long id;
        private String title;
        private String createdAt;

        public NoticeSimpleInfo (Notice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.createdAt = Time.calculateTime(notice.getCreatedAt(), 1); // yyyy-mm-dd 형식
        }
    }

    @Getter
    @AllArgsConstructor
    public static class NoticeDetailInfo {
        private String title;
        private String content;
        private String createdAt;

        public NoticeDetailInfo (Notice notice) {
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.createdAt = Time.calculateTime(notice.getCreatedAt(), 1); // yyyy-mm-dd 형식
        }
    }
}
