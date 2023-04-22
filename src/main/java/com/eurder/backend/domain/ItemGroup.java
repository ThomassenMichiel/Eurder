package com.eurder.backend.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "ITEM_GROUP")
public class ItemGroup {
    public static final int STANDARD_SHIPPING = 1;
    public static final int OUT_OF_STOCK_SHIPPING = 7;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ITEM_ID")
    private Item item;
    @Column(name = "PRICE")
    private BigDecimal price;
    @Column(name = "AMOUNT")
    private int amount;
    @Column(name = "SHIPPING_DATE")
    private LocalDate shippingDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    public ItemGroup(Item item, int amount) {
        this.item = item;
        this.amount = amount;
        calculateShippingDate();
    }

    public ItemGroup(Item item, int amount, BigDecimal price) {
        this.item = item;
        this.price = price;
        this.amount = amount;
        calculateShippingDate();
    }

    public ItemGroup() {

    }

    public ItemGroup(Long id, Item item, int amount, LocalDate shippingDate) {
        this.id = id;
        this.item = item;
        this.amount = amount;
        this.shippingDate = shippingDate;
    }

    public Long getId() {
        return id;
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
        return price.multiply(BigDecimal.valueOf(amount));
    }

    private void calculateShippingDate() {
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

    public void setOrder(Order order) {
        this.order = order;
    }
}
