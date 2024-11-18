package com.sparta.gaeppa.global.exception;

import lombok.Getter;

@Getter
public class DefaultException extends RuntimeException {
    private final ExceptionStatus exceptionStatus;

    public DefaultException(ExceptionStatus exceptionStatus) {
        this.exceptionStatus = exceptionStatus;
    }

    @Override
    public String getMessage() {
        return exceptionStatus.getMessage();
    }

    public int getStatus() {
        return exceptionStatus.getStatus();
    }
}
