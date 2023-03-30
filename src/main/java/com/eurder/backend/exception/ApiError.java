package com.eurder.backend.exception;

public class ApiError {
    private final String title;
    private final String message;
    private final String status;
    private final int code;

    public ApiError(String title, String message, String status, int code) {
        this.title = title;
        this.message = message;
        this.status = status;
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }
}

