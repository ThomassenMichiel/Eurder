package com.eurder.backend.service;

import com.eurder.backend.domain.Customer;
import com.eurder.backend.domain.Order;
import com.eurder.backend.dto.reponse.CreatedOrderDto;
import com.eurder.backend.dto.reponse.OrderDto;
import com.eurder.backend.dto.reponse.OrderListDto;
import com.eurder.backend.dto.request.CreateOrderDto;
import com.eurder.backend.exception.OrderNotFoundException;
import com.eurder.backend.exception.ForbiddenException;
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

import static com.eurder.backend.util.OrderUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        when(mapper.toCreatedOrderDto(order)).thenReturn(createdOrderDto);

        CreatedOrderDto answer = service.save(given);

        assertThat(answer).isEqualTo(createdOrderDto);

        verify(mapper).toDomain(any());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Find all orders for the current user")
    void findAllForCurrentUser() {
        Customer customer = CustomerUtil.joe(1L);
        when(customerService.getCurrentUser()).thenReturn(customer);
        List<Order> orders = List.of(firstOrder(1L), OrderUtil.secondOrder(2L));
        when(repository.findAllByCustomer(customer)).thenReturn(orders);

        List<OrderDto> orderDtos = orders.stream().map(mapper::toDto).toList();
        OrderListDto orderListDto = new OrderListDto(orderDtos, firstOrder(1L).getPrice().doubleValue() + secondOrder(2L).getPrice().doubleValue());

        OrderListDto answer = service.findAll();

        assertThat(answer).isEqualTo(orderListDto);
    }

    @Test
    @DisplayName("Reorder a previous order")
    void reorder() {
        Long id = 1L;
        CreatedOrderDto createdOrderDto = new CreatedOrderDto(1L, URI.create("/orders/" + id), firstOrder().getPrice().doubleValue());
        when(repository.findById(id)).thenReturn(Optional.of(firstOrder()));
        when(repository.save(any())).thenReturn(firstOrder(1L));
        when(mapper.toCreatedOrderDto(firstOrder())).thenReturn(createdOrderDto);
        when(customerService.getCurrentUser()).thenReturn(firstOrder().getCustomer());

        CreatedOrderDto answer = service.reorder(1L);

        assertThat(answer).isEqualTo(createdOrderDto);
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(any());
        verify(mapper, times(1)).toCreatedOrderDto(any());
        verify(customerService, times(2)).getCurrentUser();
    }

    @Test
    @DisplayName("Reorder a previous order - order not found")
    void reorder_orderNotFound() {
        Long id = 1L;
        CreatedOrderDto createdOrderDto = new CreatedOrderDto(1L, URI.create("/orders/" + id), firstOrder().getPrice().doubleValue());
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.reorder(1L))
                .hasMessage("Order not found")
                .isInstanceOf(OrderNotFoundException.class);


        verify(repository, times(1)).findById(id);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper);
        verifyNoInteractions(customerService);
    }

    @Test
    @DisplayName("Reorder a previous order - unauthorized")
    void reorder_unauthorized() {
        Long id = 1L;
        CreatedOrderDto createdOrderDto = new CreatedOrderDto(1L, URI.create("/orders/" + id), firstOrder().getPrice().doubleValue());
        when(repository.findById(id)).thenReturn(Optional.of(firstOrder()));
        when(customerService.getCurrentUser()).thenReturn(secondOrder().getCustomer());

        assertThatThrownBy(() -> service.reorder(1L))
                .hasMessage("You have no access to this resource")
                .isInstanceOf(ForbiddenException.class);

        verify(repository, times(1)).findById(id);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper);
        verify(customerService, times(1)).getCurrentUser();
        verifyNoMoreInteractions(customerService);
    }
}
