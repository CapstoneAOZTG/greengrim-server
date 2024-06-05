package com.greengrim.green.common.exception.errorCode;

import com.greengrim.green.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum EventErrorCode implements ErrorCode {

    EMPTY_EVENT("EVENT_001", "이벤트가 없습니다.", HttpStatus.CONFLICT)
    ;
    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}