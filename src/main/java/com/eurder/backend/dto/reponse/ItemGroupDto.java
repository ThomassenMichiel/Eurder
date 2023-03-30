package com.eurder.backend.dto.reponse;

import java.util.Objects;

public class ItemGroupDto {
    private String itemName;
    private int orderedAmount;
    private double price;

    public ItemGroupDto() {
    }

    public ItemGroupDto(String itemName, int orderedAmount, double price) {
        this.itemName = itemName;
        this.orderedAmount = orderedAmount;
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public int getOrderedAmount() {
        return orderedAmount;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemGroupDto that)) return false;
        return orderedAmount == that.orderedAmount && Double.compare(that.price, price) == 0 && Objects.equals(itemName, that.itemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemName, orderedAmount, price);
    }
}
