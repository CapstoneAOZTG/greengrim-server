package com.greengrim.green.core.report;

import com.greengrim.green.common.entity.BaseTime;
import com.greengrim.green.core.member.Member;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class Report extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long resourceId; // 신고 대상 id

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReportType type; // 신고 대상 type

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReportReason reportReason; // 신고 사유

    @Nullable
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

}
