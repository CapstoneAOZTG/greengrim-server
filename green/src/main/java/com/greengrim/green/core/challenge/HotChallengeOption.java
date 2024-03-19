package com.greengrim.green.core.challenge;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum HotChallengeOption {

    MOST_CHATTING(1, "활발한 채팅!", "일주일 내 가장 채팅이 많은"),
    MOST_CERTIFICATION(2, "활발한 인증!", "일주일 내 인증이 가장 많은"),
    MOST_RECENT(3, " 분 전 신설!", "가장 최근에 만들어진"),
    MOST_HEADCOUNT(4, " 명이 참여 중!", "참여 인원이 가장 많은"),
    CLOSE_TO_CLOSING(5, "마감 임박! 남은 티켓 ", "마감 임박한");

    private final int index;
    private final String subTitle;
    private final String option;
}
