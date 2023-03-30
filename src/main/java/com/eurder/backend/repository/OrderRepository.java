package com.eurder.backend.repository;

import com.eurder.backend.domain.Customer;
import com.eurder.backend.domain.Order;
import com.eurder.backend.domain.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public non-sealed class OrderRepository extends AbstractCrud<Order, Long> {
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Order save(Order order) {
        Order orderWithNewId = new Order(idCounter.getAndIncrement(), order.getItemGroups(), order.getCustomer());
        repository.put(orderWithNewId.getId(), orderWithNewId);
        return orderWithNewId;
    }

    public List<Order> findAllByCustomer(Customer customer) {
        return repository.values().stream().filter(order -> order.getCustomer().equals(customer)).toList();
    }
}
