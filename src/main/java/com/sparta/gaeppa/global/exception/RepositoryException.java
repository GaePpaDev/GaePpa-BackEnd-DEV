package com.sparta.gaeppa.global.exception;

public class RepositoryException extends DefaultException {
    public RepositoryException(ExceptionStatus exceptionStatus) {
        super(exceptionStatus);
    }
}
