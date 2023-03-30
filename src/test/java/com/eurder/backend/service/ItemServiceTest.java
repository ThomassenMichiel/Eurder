package com.eurder.backend.service;

import com.eurder.backend.domain.Item;
import com.eurder.backend.dto.request.CreateItemDto;
import com.eurder.backend.exception.ItemNotFoundException;
import com.eurder.backend.mapper.ItemMapper;
import com.eurder.backend.repository.ItemRepository;
import com.eurder.backend.util.ItemUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        Item item = ItemUtil.orange();
        Item itemAfterCreation = ItemUtil.orange(10L);
        CreateItemDto given = ItemUtil.createItemDto(ItemUtil.orange());
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
        when(repository.findById(id)).thenReturn(Optional.of(ItemUtil.apple(id)));

        Item answer = service.findById(id);

        assertThat(answer).isEqualTo(ItemUtil.apple(10L));

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
}
