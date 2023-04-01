package com.eurder.backend.dto.reponse;

import java.util.List;
import java.util.Objects;

public class ItemDtoList {
    private List<ItemDto> itemList;

    public ItemDtoList() {
    }

    public ItemDtoList(List<ItemDto> itemList) {
        this.itemList = itemList;
    }

    public List<ItemDto> getItemList() {
        return itemList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemDtoList that)) return false;
        return Objects.equals(itemList, that.itemList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemList);
    }

    @Override
    public String toString() {
        return "ItemDtoList{" +
                "itemList=" + itemList +
                '}';
    }
}
