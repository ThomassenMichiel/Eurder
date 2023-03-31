package com.eurder.backend.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException{
    public NotFoundException(String title, String message) {
        super(HttpStatus.NOT_FOUND, title, message);
    }
}
