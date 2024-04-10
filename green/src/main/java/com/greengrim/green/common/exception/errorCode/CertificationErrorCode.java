package com.greengrim.green.common.exception.errorCode;

import com.greengrim.green.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CertificationErrorCode implements ErrorCode {
    EMPTY_CERTIFICATION("CERTIFICATION_001", "존재하지 않는 인증입니다.", HttpStatus.CONFLICT),
    NO_AUTHORIZATION("CERTIFICATION_002", "인증에 대해 권한이 없습니다.", HttpStatus.CONFLICT)
    ;

    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
