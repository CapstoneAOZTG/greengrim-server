package com.greengrim.green.core.report.dto;

import com.greengrim.green.core.report.entity.ReportReason;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReportRequestDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegisterReport {
        @NotNull(message = "신고 대상의 ID를 입력해야합니다.")
        private Long resourceId;
        @NotNull(message = "신고 사유를 선택해야합니다.")
        private ReportReason reason;
        @NotNull(message = "신고 사유를 입력해야합니다.")
        private String content;
    }
}
