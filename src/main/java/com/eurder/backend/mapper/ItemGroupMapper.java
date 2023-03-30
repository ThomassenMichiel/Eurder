package com.eurder.backend.mapper;

import com.eurder.backend.domain.Item;
import com.eurder.backend.domain.ItemGroup;
import com.eurder.backend.dto.request.CreateItemGroupDto;
import com.eurder.backend.service.ItemService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ItemGroupMapper {
    private final ItemService itemService;

    public ItemGroupMapper(ItemService itemService) {
        this.itemService = itemService;
    }

    public ItemGroup toDomain(CreateItemGroupDto createItemGroupDto) {
        Item item = itemService.findById(createItemGroupDto.getItemId());
        return new ItemGroup(item, createItemGroupDto.getAmount(), LocalDate.now().plusDays(1));
    }
}
