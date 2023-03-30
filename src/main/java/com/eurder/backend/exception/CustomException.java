package com.eurder.backend.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final String title;
    private final String message;
    
    public CustomException(HttpStatus status, String title, String message) {
        super(message);
        this.status = status;
        this.title = title;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
