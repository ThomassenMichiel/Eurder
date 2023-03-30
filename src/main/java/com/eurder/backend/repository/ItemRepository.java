package com.eurder.backend.repository;

import com.eurder.backend.domain.Item;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public non-sealed class ItemRepository extends AbstractCrud<Item, Long> {
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Item save(Item item) {
        Item itemWithNewId = new Item(idCounter.getAndIncrement(), item.getName(), item.getDescription(), item.getPrice(), item.getAmount());
        this.repository.put(itemWithNewId.getId(), itemWithNewId);
        return itemWithNewId;
    }
}
