package com.foxminded.university.repository.exception;

public class LogicalException extends RuntimeException {
    public LogicalException() {
    }

    public LogicalException(String message) {
        super(message);
    }

    public LogicalException(String message, Throwable cause) {
        super(message, cause);
    }

}
