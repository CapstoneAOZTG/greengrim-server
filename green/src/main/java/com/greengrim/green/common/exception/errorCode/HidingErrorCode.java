package com.greengrim.green.common.exception.errorCode;

import com.greengrim.green.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HidingErrorCode implements ErrorCode {

    ALREADY_HIDING("HIDING_001","이미 차단한 리소스입니다.", HttpStatus.FORBIDDEN),
    ;

    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
