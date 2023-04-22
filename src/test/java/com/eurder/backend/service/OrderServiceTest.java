package com.eurder.backend.service;

import com.eurder.backend.domain.Customer;
import com.eurder.backend.domain.Order;
import com.eurder.backend.dto.reponse.CreatedOrderDto;
import com.eurder.backend.dto.reponse.OrderDto;
import com.eurder.backend.dto.reponse.OrderListDto;
import com.eurder.backend.dto.request.CreateOrderDto;
import com.eurder.backend.mapper.OrderMapper;
import com.eurder.backend.repository.OrderRepository;
import com.eurder.backend.util.CustomerUtil;
import com.eurder.backend.util.OrderUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.eurder.backend.util.CustomerUtil.jack;
import static com.eurder.backend.util.OrderUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository repository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private CustomerService customerService;
    @InjectMocks
    private OrderService service;

    @Test
    @DisplayName("Save method")
    void save() {
        CreatedOrderDto createdOrderDto = new CreatedOrderDto(1L, URI.create("/orders/1"), firstOrder().getPrice().doubleValue());
        CreateOrderDto orderDto = createOrderDto(firstOrder());
        when(orderMapper.toDomain(any())).thenReturn(firstOrder());
        when(orderMapper.toCreatedOrderDto(any())).thenReturn(createdOrderDto);

        CreatedOrderDto answer = service.save(orderDto);

        assertThat(answer).isEqualTo(createdOrderDto);
    }

    @Test
    @DisplayName("Find all")
    void findALl() {
        List<OrderDto> dto = List.of(toDto(firstOrder()), toDto(secondOrder()));
        OrderListDto expected = new OrderListDto(dto, firstOrder().getPrice().add(secondOrder().getPrice()).doubleValue());
        List<Order> domain = List.of(firstOrder(), secondOrder());
        Customer customer = CustomerUtil.bobby(1L);
        when(customerService.getCurrentUser()).thenReturn(customer);
        when(repository.findAllByCustomer(customer)).thenReturn(domain);
        when(orderMapper.toDto(any(Order.class))).thenReturn(toDto(firstOrder()), toDto(secondOrder()));

        OrderListDto answer = service.findAll();

        assertThat(answer).isEqualTo(expected);
    }

    @Test
    @DisplayName("Reorder")
    void reorder() {
        CreatedOrderDto createdOrderDto = new CreatedOrderDto(2L, URI.create("/orders/2"), firstOrder().getPrice().doubleValue());
        when(repository.findById(anyLong())).thenReturn(Optional.of(OrderUtil.firstOrder(1L)));
        when(customerService.getCurrentUser()).thenReturn(OrderUtil.firstOrder().getCustomer());
        when(orderMapper.toDomain(any())).thenReturn(firstOrder(1L));
        when(orderMapper.toDomain(any())).thenReturn(firstOrder());
        when(orderMapper.toCreatedOrderDto(any())).thenReturn(createdOrderDto);
        CreatedOrderDto expected = new CreatedOrderDto(2L, URI.create("/orders/2"), firstOrder().getPrice().doubleValue());

        CreatedOrderDto answer = service.reorder(1L);

        assertThat(answer).isEqualTo(expected);
    }

    @Test
    @DisplayName("Reorder - wrong customer")
    void reorder_wrongCustomer() {
        when(customerService.getCurrentUser()).thenReturn(jack());
        when(repository.findById(1L)).thenReturn(Optional.of(OrderUtil.firstOrder()));

        assertThatThrownBy(() -> service.reorder(1L))
                .hasMessage("You have no access to this resource");
    }

    @Test
    @DisplayName("Find all items to ship today")
    void findAllItemsToShipToday() {
        when(repository.findAll()).thenReturn(List.of(firstOrder(1L), secondOrder(2L)));
    }
}
