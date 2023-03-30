package com.eurder.backend.repository;

import com.eurder.backend.domain.Order;
import com.eurder.backend.util.OrderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryTest {
    private OrderRepository repository;

    @BeforeEach
    void setUp() {
        repository = new OrderRepository();
    }

    @Test
    @DisplayName("Save Order")
    void save() {
        Order given = OrderUtil.firstOrder();
        Order expected = OrderUtil.firstOrder(1L);

        Order answer = repository.save(given);

        assertThat(answer).isEqualTo(expected);
        assertThat(answer.getId()).isEqualTo(expected.getId());

    }

    @Test
    @DisplayName("Save Orders given already existing Orders")
    void saveOrder_givenAlreadyExistingOrders() {
        Order Order1 = OrderUtil.firstOrder();
        Order Order2 = OrderUtil.secondOrder();
        Order Order3 = OrderUtil.thirdOrder();
        repository.save(Order1);
        repository.save(Order2);
        repository.save(Order3);

        Order given = OrderUtil.fourthOrder();
        Order expected = OrderUtil.fourthOrder(4L);

        Order answer = repository.save(given);

        assertThat(answer).isEqualTo(expected);
        assertThat(answer.getId()).isEqualTo(expected.getId());
    }
}
