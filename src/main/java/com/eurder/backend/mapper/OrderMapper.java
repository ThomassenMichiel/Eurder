package com.eurder.backend.mapper;

import com.eurder.backend.domain.ItemGroup;
import com.eurder.backend.domain.Order;
import com.eurder.backend.dto.reponse.CreatedOrderDto;
import com.eurder.backend.dto.reponse.OrderDto;
import com.eurder.backend.dto.request.CreateOrderDto;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Component
public class OrderMapper {
    private final ItemGroupMapper itemGroupMapper;

    public OrderMapper(ItemGroupMapper itemGroupMapper) {
        this.itemGroupMapper = itemGroupMapper;
    }

    public Order toDomain(CreateOrderDto orderDto) {
        List<ItemGroup> list = orderDto.getItemGroupList().stream().map(itemGroupMapper::toDomain).toList();
        return new Order(list);
    }

    public CreatedOrderDto toCreatedOrderDto(Order order) {
        return new CreatedOrderDto(order.getId(), URI.create("/orders/" + order.getId()), order.getPrice().doubleValue());
    }

    public OrderDto toDto(Order order) {
        return new OrderDto(order.getId(), order.getItemGroups().stream().map(itemGroupMapper::toDto).toList(), order.getPrice().doubleValue());
    }
}
