package com.eurder.backend.mapper;

import com.eurder.backend.domain.ItemGroup;
import com.eurder.backend.domain.Order;
import com.eurder.backend.dto.reponse.CreatedOrderDto;
import com.eurder.backend.dto.request.CreateOrderDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

    public CreatedOrderDto toDto(Order order) {
        BigDecimal price = order.getPrice();
        double v = price.doubleValue();
        return new CreatedOrderDto(order.getId(), URI.create("/orders/" + order.getId()), order.getPrice().doubleValue());
    }
}
