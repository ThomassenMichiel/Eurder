package com.eurder.backend.dto.reponse;

import java.util.List;
import java.util.Objects;

public class OrderDto {
    private Long id;
    private List<ItemGroupDto> itemGroups;
    private double price;

    public OrderDto() {
    }

    public OrderDto(Long id, List<ItemGroupDto> itemGroups, double price) {
        this.id = id;
        this.itemGroups = itemGroups;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public List<ItemGroupDto> getItemGroups() {
        return itemGroups;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDto orderDto)) return false;
        return Double.compare(orderDto.price, price) == 0 && Objects.equals(id, orderDto.id) && Objects.equals(itemGroups, orderDto.itemGroups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemGroups, price);
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", itemGroups=" + itemGroups +
                ", price=" + price +
                '}';
    }
}
