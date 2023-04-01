package com.eurder.backend.dto.reponse;

import java.util.List;

public class ItemsToShipDto {
    private List<ItemGroupDto> items;
    private AddressDto address;

    public ItemsToShipDto() {
    }

    public ItemsToShipDto(List<ItemGroupDto> items, AddressDto address) {
        this.items = items;
        this.address = address;
    }

    public List<ItemGroupDto> getItems() {
        return items;
    }

    public AddressDto getAddress() {
        return address;
    }
}
