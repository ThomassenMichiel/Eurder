package com.eurder.backend.repository;

import com.eurder.backend.domain.Customer;
import com.eurder.backend.domain.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public non-sealed class CustomerRepository extends AbstractCrud<Customer, Long> {
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Customer save(Customer customer) {
        Customer customerWithNewId = new Customer(idCounter.getAndIncrement(), customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getAddress(), customer.getPhoneNumber(), customer.getPassword(), List.of(new Role("USER")));
        repository.put(customerWithNewId.getId(), customerWithNewId);
        return customerWithNewId;
    }

    public Optional<Customer> findByUsername(String username) {
        return repository.values().stream().filter(customer -> customer.getUsername().equals(username)).findFirst();
    }
}
