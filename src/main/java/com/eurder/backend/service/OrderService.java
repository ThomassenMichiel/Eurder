package com.eurder.backend.service;

import com.eurder.backend.domain.Order;
import com.eurder.backend.dto.reponse.CreatedOrderDto;
import com.eurder.backend.dto.request.CreateOrderDto;
import com.eurder.backend.mapper.OrderMapper;
import com.eurder.backend.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository repository;
    private final OrderMapper orderMapper;
    private final CustomerService customerService;

    public OrderService(OrderRepository repository, OrderMapper orderMapper, CustomerService customerService) {
        this.repository = repository;
        this.orderMapper = orderMapper;
        this.customerService = customerService;
    }

    public CreatedOrderDto save(CreateOrderDto orderDto) {
        Order savedOrder = repository.save(orderMapper.toDomain(orderDto));
        savedOrder.setCustomer(customerService.getCurrentUser());

        savedOrder.getItemGroups().forEach(itemGroup -> {
            if (itemGroup.getItem().getAmount() - itemGroup.getAmount() <= 0) {
                itemGroup.calculateShippingDate();
            }
            itemGroup.getItem().decreaseBy(itemGroup.getAmount());
        });

        return orderMapper.toDto(savedOrder);
    }
}
