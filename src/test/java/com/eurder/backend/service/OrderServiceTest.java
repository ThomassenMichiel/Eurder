package com.eurder.backend.service;

import com.eurder.backend.domain.Order;
import com.eurder.backend.dto.reponse.CreatedOrderDto;
import com.eurder.backend.dto.request.CreateOrderDto;
import com.eurder.backend.mapper.OrderMapper;
import com.eurder.backend.repository.OrderRepository;
import com.eurder.backend.util.OrderUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.URI;

import static com.eurder.backend.util.OrderUtil.firstOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository repository;
    @Mock
    private OrderMapper mapper;
    @Mock
    private CustomerService customerService;
    @InjectMocks
    private OrderService service;

    @Test
    @DisplayName("Save a order")
    void save() {
        Order order = firstOrder();
        Order orderAfterCreation = firstOrder(10L);
        CreateOrderDto given = OrderUtil.createOrderDto(order);
        CreatedOrderDto createdOrderDto = new CreatedOrderDto(orderAfterCreation.getId(), URI.create("/orders/" + orderAfterCreation.getId()), orderAfterCreation.getPrice().doubleValue());
        when(repository.save(order)).thenReturn(orderAfterCreation);
        when(mapper.toDomain(given)).thenReturn(order);
        when(customerService.getCurrentUser()).thenReturn(order.getCustomer());
        when(mapper.toDto(order)).thenReturn(createdOrderDto);




        CreatedOrderDto answer = service.save(given);

        assertThat(answer).isEqualTo(createdOrderDto);

        verify(mapper).toDomain(any());
        verify(repository).save(any());
    }
}
