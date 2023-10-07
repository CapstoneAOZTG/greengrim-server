package com.greengrim.green.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getErrorCode();
    String getMessage();
    HttpStatus getStatus();
}
