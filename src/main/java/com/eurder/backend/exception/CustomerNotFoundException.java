package com.eurder.backend.exception;

import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends CustomException {
    public CustomerNotFoundException() {
        super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(), "Customer not found");
    }
}
