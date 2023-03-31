package com.eurder.backend.exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends NotFoundException {
    public OrderNotFoundException() {
        super("Order not found", "Order not found");
    }
}
