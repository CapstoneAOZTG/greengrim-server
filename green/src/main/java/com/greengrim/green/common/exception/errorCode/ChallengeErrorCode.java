package com.greengrim.green.common.exception.errorCode;

import com.greengrim.green.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ChallengeErrorCode implements ErrorCode {
    EMPTY_CHALLENGE("CHALLENGE_001", "존재하지 않는 챌린지입니다.", HttpStatus.CONFLICT),
    OVER_CAPACITY_CHALLENGE("CHALLENGE_002", "수용인원이 초과된 챌린지입니다.", HttpStatus.CONFLICT),
    ALREADY_ENTERED_CHALLENGE("CHALLENGE_003", "이미 참여중인 챌린지입니다.", HttpStatus.CONFLICT),
    NO_AUTHORIZATION("CHALLENGE_004", "챌린지에 대해 권한이 없습니다.", HttpStatus.UNAUTHORIZED)
    ;

    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
