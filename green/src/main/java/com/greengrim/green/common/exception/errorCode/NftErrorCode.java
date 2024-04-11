package com.greengrim.green.common.exception.errorCode;

import com.greengrim.green.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NftErrorCode implements ErrorCode {
    INVALID_NFT("NFT_001", "유효하지 않은 NFT입니다.", HttpStatus.CONFLICT),
    EMPTY_NFT("NFT_002", "교환 가능한 NFT의 개수가 부족합니다.", HttpStatus.CONFLICT),
    NO_AUTHORIZATION("NFT_003", "NFT에 대해 권한이 없습니다.", HttpStatus.CONFLICT)
    ;
    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}
