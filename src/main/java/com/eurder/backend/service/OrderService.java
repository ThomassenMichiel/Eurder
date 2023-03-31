package com.eurder.backend.service;

import com.eurder.backend.domain.Customer;
import com.eurder.backend.domain.Order;
import com.eurder.backend.dto.reponse.CreatedOrderDto;
import com.eurder.backend.dto.reponse.OrderListDto;
import com.eurder.backend.dto.request.CreateItemGroupDto;
import com.eurder.backend.dto.request.CreateOrderDto;
import com.eurder.backend.exception.OrderNotFoundException;
import com.eurder.backend.exception.ForbiddenException;
import com.eurder.backend.mapper.OrderMapper;
import com.eurder.backend.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

        return orderMapper.toCreatedOrderDto(savedOrder);
    }

    public OrderListDto findAll() {
        Customer customer = customerService.getCurrentUser();
        List<Order> allOrders = repository.findAllByCustomer(customer);
        double totalPrice = allOrders.stream().map(Order::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
        return new OrderListDto(allOrders.stream().map(orderMapper::toDto).toList(), totalPrice);
    }

    public CreatedOrderDto reorder(Long previousOrderId) {
        Order previousOrder = repository.findById(previousOrderId)
                .orElseThrow(OrderNotFoundException::new);

        if (!customerService.getCurrentUser().equals(previousOrder.getCustomer())) {
            throw new ForbiddenException();
        }

        List<CreateItemGroupDto> itemGroupDtos = previousOrder.getItemGroups().stream().map(itemGroup -> new CreateItemGroupDto(itemGroup.getItem().getId(), itemGroup.getAmount())).toList();
        return save(new CreateOrderDto(itemGroupDtos));
    }
}
