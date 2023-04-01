package com.eurder.backend.dto.reponse;

import java.util.List;
import java.util.Objects;

public class ItemsToShipListDto {
    private List<ItemsToShipDto> items;

    public ItemsToShipListDto() {
    }

    public ItemsToShipListDto(List<ItemsToShipDto> items) {
        this.items = items;
    }

    public List<ItemsToShipDto> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemsToShipListDto that)) return false;
        return Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
}
