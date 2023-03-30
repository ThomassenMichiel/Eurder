package com.eurder.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public class UpdateItemDto {
    @NotNull(message = "ID cannot be null")
    @Min(value = 0, message = "ID cannot be lower than 0")
    private final Long id;
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

    public UpdateItemDto(Long id, String name, String description, double price, int amount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }

    public Long getId() {
        return id;
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
