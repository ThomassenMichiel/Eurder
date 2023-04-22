package com.eurder.backend.repository;

import com.eurder.backend.domain.Customer;
import com.eurder.backend.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByCustomer(Customer customer);
}
