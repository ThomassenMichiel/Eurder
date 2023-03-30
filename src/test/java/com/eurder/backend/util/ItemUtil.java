package com.eurder.backend.util;

import com.eurder.backend.domain.Item;
import com.eurder.backend.dto.request.CreateItemDto;
import com.eurder.backend.dto.request.UpdateItemDto;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public final class ItemUtil {
    public static Item apple() {
        return apple(null);
    }

    public static Item apple(Long id) {
        return new Item(id, "Apple", "It's green", BigDecimal.valueOf(2.22d), 10);
    }

    public static Item orange() {
        return orange(null);
    }

    public static Item orange(Long id) {
        return new Item(id, "Orange", "It's orange", BigDecimal.valueOf(6.66d), 9);
    }

    public static Item banana() {
        return banana(null);
    }

    public static Item banana(Long id) {
        return new Item(id, "Banana", "It's banana", BigDecimal.valueOf(4.20d), 0);
    }

    public static Item strawberry() {
        return strawberry(null);
    }

    public static Item strawberry(Long id) {
        return new Item(id, "Strawberry", "It's red", BigDecimal.valueOf(6.9d), 6);
    }

    public static CreateItemDto createItemDto(Item item) {
        return new CreateItemDto(item.getName(), item.getDescription(), item.getPrice().doubleValue(), item.getAmount());
    }

    public static UpdateItemDto updateItemDto(Item item) {
        if (item.getId() == null || item.getId() < 0) {
            fail("UpdateItemDto has no ID provided or is smaller than 0");
        }
        return new UpdateItemDto(item.getId(), item.getName(), item.getDescription(), item.getPrice().doubleValue(), item.getAmount());
    }
}
