package com.eurder.backend.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends CustomException {
    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN, "Forbidden", "You have no access to this resource");
    }
}
