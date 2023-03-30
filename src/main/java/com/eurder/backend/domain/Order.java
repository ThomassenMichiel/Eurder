package com.eurder.backend.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Order {
    private Long id;
    private final List<ItemGroup> itemGroups;
    private Customer customer;

    public Order(List<ItemGroup> itemGroups) {
        this.itemGroups = itemGroups;
    }

    public Order(Long id, List<ItemGroup> itemGroups, Customer customer) {
        this.id = id;
        this.itemGroups = itemGroups;
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public List<ItemGroup> getItemGroups() {
        return itemGroups;
    }

    public BigDecimal getPrice() {
        return itemGroups.stream().map(ItemGroup::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(itemGroups, order.itemGroups) && Objects.equals(customer, order.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemGroups, customer);
    }
}
