package com.eurder.backend.exception;

import org.springframework.http.HttpStatus;

public class ItemNotFoundException extends NotFoundException {
    public ItemNotFoundException() {
        super("Item not found", "Item not found");
    }
}
