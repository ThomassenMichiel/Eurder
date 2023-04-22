package com.eurder.backend.mapper;

import com.eurder.backend.domain.ItemToShip;
import com.eurder.backend.domain.Order;
import com.eurder.backend.dto.reponse.*;
import com.eurder.backend.dto.request.CreateOrderDto;
import com.eurder.backend.service.CustomerService;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Component
public class OrderMapper {
    private final ItemGroupMapper itemGroupMapper;
    private final AddressMapper addressMapper;
    private final CustomerService customerService;

    public OrderMapper(ItemGroupMapper itemGroupMapper, AddressMapper addressMapper, CustomerService customerService) {
        this.itemGroupMapper = itemGroupMapper;
        this.addressMapper = addressMapper;
        this.customerService = customerService;
    }

    public Order toDomain(CreateOrderDto orderDto) {
        Order order = new Order();
        orderDto.getItemGroupList().stream().map(itemGroupMapper::toDomain).forEach(order::addItemGroup);
        order.setCustomer(customerService.getCurrentUser());
        return order;
    }

    public CreatedOrderDto toCreatedOrderDto(Order order) {
        return new CreatedOrderDto(order.getId(), URI.create("/orders/" + order.getId()), order.getPrice().doubleValue());
    }

    public OrderDto toDto(Order order) {
        return new OrderDto(order.getId(), order.getItemGroups().stream().map(itemGroupMapper::toDto).toList(), order.getPrice().doubleValue());
    }

    public ItemsToShipListDto toDto(List<ItemToShip> listOfItemsToShip) {
        List<ItemsToShipDto> itemsToShip = listOfItemsToShip.stream().map(itemToShip -> {
            List<ItemGroupDto> itemGroupDtoList = itemToShip.getItems().stream().filter(itemGroup -> itemGroup.getShippingDate().equals(LocalDate.now())).map(itemGroupMapper::toDto).toList();
            AddressDto addressDto = addressMapper.toDto(itemToShip.getAddress());
            return new ItemsToShipDto(itemGroupDtoList, addressDto);
        }).toList();
        return new ItemsToShipListDto(itemsToShip);
    }
}
