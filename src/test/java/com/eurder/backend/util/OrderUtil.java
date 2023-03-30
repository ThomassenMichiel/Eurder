package com.eurder.backend.util;

import com.eurder.backend.domain.ItemGroup;
import com.eurder.backend.domain.Order;
import com.eurder.backend.dto.request.CreateItemDto;
import com.eurder.backend.dto.request.CreateItemGroupDto;
import com.eurder.backend.dto.request.CreateOrderDto;

import java.time.LocalDate;
import java.util.List;

public final class OrderUtil {
    public static Order firstOrder() {
        return firstOrder(null);
    }

    public static Order firstOrder(Long id) {
        ItemGroup itemGroup = new ItemGroup(ItemUtil.apple(1L), 1, LocalDate.now().plusDays(1));
        return new Order(id, List.of(itemGroup), CustomerUtil.bobby(1L));
    }

    public static Order secondOrder() {
        return secondOrder(null);
    }

    public static Order secondOrder(Long id) {
        ItemGroup itemGroup = new ItemGroup(ItemUtil.banana(2L), 15, LocalDate.now().plusDays(1));
        return new Order(id, List.of(itemGroup), CustomerUtil.jack(2L));
    }

    public static Order thirdOrder() {
        return thirdOrder(null);
    }

    public static Order thirdOrder(Long id) {
        ItemGroup itemGroup = new ItemGroup(ItemUtil.strawberry(3L), 0, LocalDate.now().plusDays(7));
        return new Order(id, List.of(itemGroup), CustomerUtil.john(3L));
    }

    public static Order fourthOrder() {
        return fourthOrder(null);
    }

    public static Order fourthOrder(Long id) {
        ItemGroup itemGroup = new ItemGroup(ItemUtil.orange(4L), 0, LocalDate.now().plusDays(7));
        return new Order(id, List.of(itemGroup), CustomerUtil.joe(4L));
    }

    public static CreateOrderDto createOrderDto(Order order) {
        CreateItemGroupDto createItemGroupDto = order.getItemGroups().stream().map(itemGroup -> new CreateItemGroupDto(itemGroup.getItem().getId(), itemGroup.getAmount())).findFirst().get();
        return new CreateOrderDto(List.of(createItemGroupDto));
    }
}
