package com.eurder.backend.repository;

import com.eurder.backend.domain.Item;
import com.eurder.backend.util.ItemUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRepositoryTest {
    private ItemRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ItemRepository();
    }

    @Test
    @DisplayName("Save Item")
    void save() {
        Item given = ItemUtil.apple();
        Item expected = ItemUtil.apple(1L);

        Item answer = repository.save(given);

        assertThat(answer).isEqualTo(expected);
        assertThat(answer.getId()).isEqualTo(expected.getId());

    }

    @Test
    @DisplayName("Save Items given already existing Items")
    void saveItem_givenAlreadyExistingItems() {
        Item item1 = ItemUtil.apple();
        Item item2 = ItemUtil.orange();
        Item item3 = ItemUtil.strawberry();
        repository.save(item1);
        repository.save(item2);
        repository.save(item3);

        Item given = ItemUtil.banana();
        Item expected = ItemUtil.banana(4L);

        Item answer = repository.save(given);

        assertThat(answer).isEqualTo(expected);
        assertThat(answer.getId()).isEqualTo(expected.getId());
    }
}
