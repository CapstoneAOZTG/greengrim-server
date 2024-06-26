package com.greengrim.green.common.exception.errorCode;

import com.greengrim.green.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    EMPTY_MEMBER("MEMBER_001", "존재하지 않는 사용자입니다.", HttpStatus.CONFLICT),
    DUPLICATE_MEMBER("MEMBER_002", "중복된 사용자입니다.", HttpStatus.CONFLICT),
    DUPLICATE_NICKNAME("MEMBER_003", "중복된 닉네임입니다.", HttpStatus.CONFLICT),
    UN_REGISTERED_MEMBER("MEMBER_004", "등록되지 않은 이메일입니다.", HttpStatus.CONFLICT),
    NO_AUTHORIZATION("MEMBER_005", "권한이 없는 사용자입니다.", HttpStatus.CONFLICT)
    ;

    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
