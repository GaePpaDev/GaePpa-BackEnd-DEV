package com.sparta.gaeppa.global.exception;

public class ControllerException extends DefaultException {
    public ControllerException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus);
    }
}
