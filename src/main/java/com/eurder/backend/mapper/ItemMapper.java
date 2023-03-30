package com.eurder.backend.mapper;

import com.eurder.backend.domain.Item;
import com.eurder.backend.dto.request.CreateItemDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ItemMapper {
    public Item toDomain(CreateItemDto createItemDto) {
        return new Item(createItemDto.getName(), createItemDto.getDescription(), BigDecimal.valueOf(createItemDto.getPrice()), createItemDto.getAmount());
    }
}
