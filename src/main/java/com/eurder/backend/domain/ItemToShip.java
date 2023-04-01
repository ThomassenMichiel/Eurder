package com.eurder.backend.domain;

import java.util.List;
import java.util.Objects;

public class ItemToShip {
    private List<ItemGroup> items;
    private Address address;

    public ItemToShip(List<ItemGroup> items, Address address) {
        this.items = items;
        this.address = address;
    }

    public List<ItemGroup> getItems() {
        return items;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemToShip that)) return false;
        return Objects.equals(items, that.items) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, address);
    }
}
