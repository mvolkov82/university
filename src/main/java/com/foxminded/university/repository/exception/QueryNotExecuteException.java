package com.foxminded.university.repository.exception;

public class QueryNotExecuteException extends RuntimeException {
    public QueryNotExecuteException(String message) {
        super(message);
    }

    public QueryNotExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryNotExecuteException() {
    }
}
