package com.greengrim.green.core.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {

    POINT_CERTIFICATION("포인트", "포인트를 획득했어요 ❗"),
    POINT_VERIFICATION("포인트", "출석체크 보상 포인트를 획득했어요 ❗"),
    NFT_EXCHANGE("NFT", "NFT 교환이 완료되었어요 \uD83E\uDEB4"),
    NFT_LIKE("NFT", "님이 내 NFT에 좋아요를 눌렀어요 \uD83D\uDC8C"),
    CHALLENGE_SUCCESS("챌린지", "챌린지에 성공했어요 \uD83C\uDF40"),
    NEW_ISSUE("환경 이슈", " \uD83E\uDEB4");

    private final String title;
    private final String content;
}
