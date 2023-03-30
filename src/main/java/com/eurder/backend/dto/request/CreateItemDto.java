package com.eurder.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public class CreateItemDto {
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private final String name;
    @NotNull(message = "Description cannot be null")
    @NotBlank(message = "Description cannot be blank")
    private final String description;
    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price cannot be lower than 0")
    private final double price;
    @NotNull(message = "Amount cannot be null")
    @Min(value = 0, message = "Amount cannot be lower than 0")
    private final int amount;

    public CreateItemDto(String name, String description, double price, int amount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }
}
