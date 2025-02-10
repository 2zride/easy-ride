package com.easyride.global.exception;

public class EasyRideException extends RuntimeException {

    private final ErrorCode errorCode;

    public EasyRideException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
