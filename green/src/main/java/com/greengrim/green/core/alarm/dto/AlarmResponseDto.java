package com.greengrim.green.core.alarm.dto;

import com.greengrim.green.core.alarm.Alarm;
import com.greengrim.green.core.alarm.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.greengrim.green.common.entity.Time.calculateTime;

public class AlarmResponseDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AlarmInfo {
        private AlarmType alarmType;
        private String alarmCategory;
        private String imgUrl;
        private String createdAt;
        private Long resourceId;
        private String variableContent;
        private String fixedContent;
        private boolean isChecked;
        private Long memberId;

        public AlarmInfo(Alarm alarm) {
            this.alarmType = alarm.getType();
            this.alarmCategory = alarm.getType().getTitle();
            this.imgUrl = alarm.getImgUrl();
            this.createdAt = calculateTime(alarm.getCreatedAt(),4);
            this.resourceId = alarm.getResourceId();
            this.variableContent = alarm.getVariableContent();
            this.fixedContent = alarm.getType().getContent();
            this.isChecked = alarm.isChecked();
            this.memberId = alarm.getMemberId();
        }
    }
}
