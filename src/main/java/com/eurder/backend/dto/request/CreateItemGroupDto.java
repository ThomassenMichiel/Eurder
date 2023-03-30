package com.eurder.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateItemGroupDto {
    @NotNull(message = "ItemId cannot be null")
    @Min(value = 0, message = "ItemId cannot be negative")
    private final Long itemId;
    @Min(value = 0, message = "Amount cannot be negative")
    private final int amount;

    public CreateItemGroupDto(Long itemId, int amount) {
        this.itemId = itemId;
        this.amount = amount;
    }

    public Long getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }
}
