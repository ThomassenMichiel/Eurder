package com.eurder.backend.exception;

import org.springframework.http.HttpStatus;

public class ItemNotFoundException extends CustomException {
    public ItemNotFoundException() {
        super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(), "Item not found");
    }
}
