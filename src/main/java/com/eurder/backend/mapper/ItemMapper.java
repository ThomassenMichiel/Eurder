package com.eurder.backend.mapper;

import com.eurder.backend.domain.Item;
import com.eurder.backend.dto.reponse.ItemDto;
import com.eurder.backend.dto.reponse.ItemDtoList;
import com.eurder.backend.dto.request.CreateItemDto;
import com.eurder.backend.dto.request.UpdateItemDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ItemMapper {
    public Item toDomain(CreateItemDto createItemDto) {
        return new Item(createItemDto.getName(), createItemDto.getDescription(), BigDecimal.valueOf(createItemDto.getPrice()), createItemDto.getAmount());
    }

    public Item toDomain(UpdateItemDto updateItemDto) {
        return new Item(updateItemDto.getId(), updateItemDto.getName(), updateItemDto.getDescription(), BigDecimal.valueOf(updateItemDto.getPrice()), updateItemDto.getAmount());
    }

    public ItemDto toDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getPrice().doubleValue(), item.getAmount(), item.getItemUrgency().name());
    }

    public ItemDtoList toDto(List<Item> itemList) {
        return new ItemDtoList(itemList.stream().map(this::toDto).toList());
    }
}
