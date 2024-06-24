package com.greengrim.green.core.history.dto;

import com.greengrim.green.core.history.entity.History;
import com.greengrim.green.core.history.entity.HistoryOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class HistoryResponseDto {

    @Getter
    @RequiredArgsConstructor
    public static class HistoryInfo {
        private Long id;
        private String title;
        private String imgUrl;
        private HistoryOption historyOption;
        private int point;
        private int totalPoint;
        private String date;

        public HistoryInfo(History history, String date) {
            this.id = history.getTargetId();
            this.title = history.getTitle();
            this.imgUrl = history.getImgUrl();
            this.historyOption = getHistoryOption();
            this.point = history.getPoint();
            this.totalPoint = history.getTotalPoint();
            this.date = date;
        }
    }
}
