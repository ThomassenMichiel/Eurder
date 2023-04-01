package com.eurder.backend.service;

import com.eurder.backend.domain.Item;
import com.eurder.backend.dto.reponse.ItemDtoList;
import com.eurder.backend.dto.request.UpdateItemDto;
import com.eurder.backend.dto.request.CreateItemDto;
import com.eurder.backend.exception.ItemNotFoundException;
import com.eurder.backend.mapper.ItemMapper;
import com.eurder.backend.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ItemService {
    private final ItemRepository repository;
    private final ItemMapper mapper;

    public ItemService(ItemRepository repository, ItemMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Long save(CreateItemDto createItemDto) {
        Item item = mapper.toDomain(createItemDto);
        Item createdItem = repository.save(item);
        return createdItem.getId();
    }

    public Item findById(Long itemId) {
        return repository.findById(itemId)
                .orElseThrow(ItemNotFoundException::new);
    }

    public void update(UpdateItemDto updateItemDto) {
        repository.save(mapper.toDomain(updateItemDto));
    }

    public ItemDtoList findAll() {
        List<Item> items = repository.findAll();
        items.sort(
                Comparator
                        .comparing(
                                Item::
                                        getItemUrgency));
        return mapper.toDto(items);
    }
}
