package com.eurder.backend.dto.reponse;

import java.util.Objects;

public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int amount;
    private String itemUrgency;

    public ItemDto() {
    }

    public ItemDto(Long id, String name, String description, double price, int amount, String itemUrgency) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
        this.itemUrgency = itemUrgency;
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

    public String getItemUrgency() {
        return itemUrgency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemDto itemDto)) return false;
        return Double.compare(itemDto.price, price) == 0 && amount == itemDto.amount && Objects.equals(id, itemDto.id) && Objects.equals(name, itemDto.name) && Objects.equals(description, itemDto.description) && Objects.equals(itemUrgency, itemDto.itemUrgency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, amount, itemUrgency);
    }

    @Override
    public String toString() {
        return "ItemDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                ", itemUrgency='" + itemUrgency + '\'' +
                '}';
    }
}
