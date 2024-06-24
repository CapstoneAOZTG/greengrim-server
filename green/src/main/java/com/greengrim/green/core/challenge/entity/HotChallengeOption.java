package com.greengrim.green.core.challenge.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HotChallengeOption {

    MOST_RECENT(1, " 신설!", "가장 최근에 만들어진"),
    MOST_HEADCOUNT(2, " 명이 참여 중!", "참여 인원이 가장 많은"),
    MOST_CERTIFICATION(3, "인증이 활발한 챌린지!", "일주일 내 인증이 가장 많은");

    private final int index;
    private final String subTitle;
    private final String option;
}
