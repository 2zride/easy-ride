package com.easyride.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 관리자에게 문의하세요."),
    ;

    private final HttpStatus status;
    private final String message;
}
