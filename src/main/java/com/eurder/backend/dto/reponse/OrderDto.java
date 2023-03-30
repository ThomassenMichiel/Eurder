package com.eurder.backend.dto.reponse;

import java.util.List;
import java.util.Objects;

public class OrderDto {
    private Long id;
    private List<ItemGroupDto> itemGroups;

    public OrderDto() {
    }

    public OrderDto(Long id, List<ItemGroupDto> itemGroups) {
        this.id = id;
        this.itemGroups = itemGroups;
    }

    public Long getId() {
        return id;
    }

    public List<ItemGroupDto> getItemGroups() {
        return itemGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDto orderDto)) return false;
        return Objects.equals(id, orderDto.id) && Objects.equals(itemGroups, orderDto.itemGroups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemGroups);
    }
}
