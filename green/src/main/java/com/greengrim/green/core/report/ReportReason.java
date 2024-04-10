package com.greengrim.green.core.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportReason {

    PROMOTIONAL_POST("부적절한 홍보"),
    LEWDNESS("음란성 또는 청소년에게 부적합한 내용"),
    HATRED("욕설 또는 혐오적 표현"),
    INFRINGEMENT("권리 침해"),
    ETC("기타");

    private final String name;

}
