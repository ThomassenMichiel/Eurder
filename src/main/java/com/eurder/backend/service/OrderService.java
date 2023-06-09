package com.eurder.backend.service;

import com.eurder.backend.domain.*;
import com.eurder.backend.dto.reponse.CreatedOrderDto;
import com.eurder.backend.dto.reponse.ItemsToShipListDto;
import com.eurder.backend.dto.reponse.OrderListDto;
import com.eurder.backend.dto.request.CreateItemGroupDto;
import com.eurder.backend.dto.request.CreateOrderDto;
import com.eurder.backend.exception.ForbiddenException;
import com.eurder.backend.exception.OrderNotFoundException;
import com.eurder.backend.mapper.OrderMapper;
import com.eurder.backend.repository.ItemGroupRepository;
import com.eurder.backend.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class OrderService {
    private final OrderRepository repository;
    private final ItemGroupRepository itemGroupRepository;
    private final OrderMapper orderMapper;
    private final CustomerService customerService;

    public OrderService(OrderRepository repository, ItemGroupRepository itemGroupRepository, OrderMapper orderMapper, CustomerService customerService) {
        this.repository = repository;
        this.itemGroupRepository = itemGroupRepository;
        this.orderMapper = orderMapper;
        this.customerService = customerService;
    }

    public CreatedOrderDto save(CreateOrderDto orderDto) {
        Order savedOrder = orderMapper.toDomain(orderDto);
        savedOrder.getItemGroups().forEach(itemGroup -> itemGroup.getItem().decreaseBy(itemGroup.getAmount()));
        return orderMapper.toCreatedOrderDto(repository.save(savedOrder));
    }

    public OrderListDto findAll() {
        Customer customer = customerService.getCurrentUser();
        List<Order> allOrders = repository.findAllByCustomer(customer);
        double totalPrice = allOrders.stream().flatMap(order -> order.getItemGroups().stream()).map(ItemGroup::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
        return new OrderListDto(allOrders.stream().map(orderMapper::toDto).toList(), totalPrice);
    }

    public CreatedOrderDto reorder(Long previousOrderId) {
        Order previousOrder = repository.findById(previousOrderId)
                .orElseThrow(OrderNotFoundException::new);

        if (!customerService.getCurrentUser().equals(previousOrder.getCustomer())) {
            throw new ForbiddenException();
        }

        List<CreateItemGroupDto> itemGroupDtos = previousOrder.getItemGroups()
                .stream()
                .map(itemGroup -> new CreateItemGroupDto(itemGroup.getItem().getId(), itemGroup.getAmount()))
                .toList();
        return save(new CreateOrderDto(itemGroupDtos));
    }

    public ItemsToShipListDto findAllItemsToShipToday() {
        List<ItemGroup> all = itemGroupRepository.findAll();
        List<ItemToShip> listOfItemsToShip = itemGroupRepository.findAllByShippingDateIs(LocalDate.now())
                .stream().map(itemGroup -> new ItemToShip(List.of(itemGroup), itemGroup.getOrder().getCustomer().getAddress()))
                .toList();
//        List<ItemToShip> listOfItemsToShip = repository.findAll()
//                .stream().map(order -> {
//                    Address address = order.getCustomer().getAddress();
//                    List<ItemGroup> items = order.getItemGroups().stream().filter(itemGroup -> itemGroup.getShippingDate().equals(LocalDate.now())).toList();
//                    return new ItemToShip(items, address);
//                }).toList();
        return orderMapper.toDto(listOfItemsToShip);
    }
}
