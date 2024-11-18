package com.sparta.gaeppa.global.exception;

public class ServiceException extends DefaultException {
    public ServiceException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus);
    }
}
