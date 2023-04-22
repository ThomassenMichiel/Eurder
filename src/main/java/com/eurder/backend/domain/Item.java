package com.eurder.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "ITEM")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    @Column(name = "NAME")
    private String name;
    @NotNull(message = "Description cannot be null")
    @NotBlank(message = "Description cannot be blank")
    @Column(name = "DESCRIPTION")
    private String description;
    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price cannot be lower than 0")
    @Column(name = "PRICE")
    private BigDecimal price;
    @Column(name = "AMOUNT")
    private int amount;
    @Column(name = "ITEM_URGENCY")
    @Enumerated(EnumType.STRING)
    private ItemUrgency itemUrgency;

    public Item() {
    }

    public Item(String name, String description, BigDecimal price, int amount) {
        this(null, name, description, price, amount);
    }

    public Item(Long id, String name, String description, BigDecimal price, int amount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
        this.itemUrgency = ItemUrgency.getByAmount(amount);
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

    public BigDecimal getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public ItemUrgency getItemUrgency() {
        return itemUrgency;
    }

    public void decreaseBy(int amountToDecrease) {
        this.amount -= amountToDecrease;
        this.itemUrgency = ItemUrgency.getByAmount(this.amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
