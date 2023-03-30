package com.eurder.backend.dto.reponse;

import java.util.List;
import java.util.Objects;

public class OrderListDto {
    private List<OrderDto> orders;
    private double totalPrice;

    public OrderListDto() {
    }

    public OrderListDto(List<OrderDto> orders, double totalPrice) {
        this.orders = orders;
        this.totalPrice = totalPrice;
    }

    public List<OrderDto> getOrders() {
        return orders;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderListDto that)) return false;
        return Double.compare(that.totalPrice, totalPrice) == 0 && Objects.equals(orders, that.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orders, totalPrice);
    }
}
