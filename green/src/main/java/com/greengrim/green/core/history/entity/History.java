package com.greengrim.green.core.history.entity;

import com.greengrim.green.common.entity.BaseTime;
import com.greengrim.green.core.history.entity.HistoryOption;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class History extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long memberId;
    @NotNull
    private Long targetId;
    @NotNull
    private String title;
    @NotNull
    private String imgUrl;
    @NotNull
    private HistoryOption historyOption;
    @NotNull
    private int point;
    @NotNull
    private int totalPoint;
}
