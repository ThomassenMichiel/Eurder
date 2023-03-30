package com.eurder.backend.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class ItemGroup {
    public static final int STANDARD_SHIPPING = 1;
    public static final int OUT_OF_STOCK_SHIPPING = 7;
    private final Item item;
    private final int amount;
    private LocalDate shippingDate;

    public ItemGroup(Item item, int amount) {
        this.item = item;
        this.amount = amount;
        calculateShippingDate();
    }

    public Item getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public BigDecimal getPrice() {
        return item.getPrice().multiply(BigDecimal.valueOf(amount));
    }

    public void calculateShippingDate() {
        int daysToAdd = STANDARD_SHIPPING;
        if ((item.getAmount() - amount) <= 0) {
            daysToAdd = OUT_OF_STOCK_SHIPPING;
        }
        shippingDate = LocalDate.now().plusDays(daysToAdd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemGroup itemGroup)) return false;
        return amount == itemGroup.amount && Objects.equals(item, itemGroup.item) && Objects.equals(shippingDate, itemGroup.shippingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, amount, shippingDate);
    }
}
