package com.eurder.backend.service;

import com.eurder.backend.domain.Item;
import com.eurder.backend.dto.reponse.ItemDto;
import com.eurder.backend.dto.reponse.ItemDtoList;
import com.eurder.backend.dto.request.CreateItemDto;
import com.eurder.backend.dto.request.UpdateItemDto;
import com.eurder.backend.exception.ItemNotFoundException;
import com.eurder.backend.mapper.ItemMapper;
import com.eurder.backend.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.eurder.backend.util.ItemUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @Mock
    private ItemRepository repository;
    @Mock
    private ItemMapper mapper;
    @InjectMocks
    private ItemService service;

    @Test
    @DisplayName("Save an item")
    void save() {
        Item item = orange();
        Item itemAfterCreation = orange(10L);
        CreateItemDto given = createItemDto(orange());
        when(repository.save(item)).thenReturn(itemAfterCreation);
        when(mapper.toDomain(given)).thenReturn(item);

        Long answer = service.save(given);

        assertThat(answer).isEqualTo(itemAfterCreation.getId());

        verify(mapper).toDomain(given);
        verify(repository).save(item);
    }

    @Test
    @DisplayName("Find by id")
    void findById() {
        Long id = 10L;
        when(repository.findById(id)).thenReturn(Optional.of(apple(id)));

        Item answer = service.findById(id);

        assertThat(answer).isEqualTo(apple(10L));

        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Find by id - not found")
    void findById_notFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        ItemNotFoundException exception = new ItemNotFoundException();

        assertThatThrownBy(() -> service.findById(10L))
                .hasMessage(exception.getMessage())
                .isInstanceOf(exception.getClass());
    }

    @Test
    @DisplayName("Update an item")
    void update() {
        Item item = orange(1L);
        UpdateItemDto updateItemDto = updateItemDto(item);
        when(mapper.toDomain(updateItemDto)).thenReturn(item);

        service.update(updateItemDto);

        verify(repository, times(1)).save(item);
    }

    @Test
    @DisplayName("Find all items")
    void findAllItems() {
        List<Item> given = new ArrayList<>(List.of(apple(), banana(), orange(), strawberry()));
        List<ItemDto> itemDtos = new ArrayList<>(List.of(toDto(banana()), toDto(strawberry()), toDto(orange()), toDto(apple())));
        ItemDtoList expected = new ItemDtoList(itemDtos);

        when(repository.findAll()).thenReturn(given);
        when(mapper.toDto(given)).thenReturn(expected);

        ItemDtoList answer = service.findAll();

        assertThat(answer).isEqualTo(expected);
    }
}
