package com.eurder.backend.exception;

import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends NotFoundException {
    public CustomerNotFoundException() {
        super("Customer not found", "Customer not found");
    }
}
