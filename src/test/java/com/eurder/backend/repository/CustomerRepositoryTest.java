package com.eurder.backend.repository;

import com.eurder.backend.domain.Address;
import com.eurder.backend.domain.Customer;
import com.eurder.backend.util.CustomerUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CustomerRepositoryTest {
    private CustomerRepository repository;

    @BeforeEach
    void setUp() {
        repository = new CustomerRepository();
    }

    @Test
    @DisplayName("Save customer")
    void save() {
        Customer given = CustomerUtil.john();
        Customer expected = CustomerUtil.john(1L);

        Customer answer = repository.save(given);

        assertThat(answer).isEqualTo(expected);
        assertThat(answer.getId()).isEqualTo(expected.getId());

    }

    @Test
    @DisplayName("Save customers given already existing customers")
    void saveCustomer_givenAlreadyExistingCustomers() {
        Customer customer1 = CustomerUtil.john();
        Customer customer2 = CustomerUtil.jack();
        Customer customer3 = CustomerUtil.joe();
        repository.save(customer1);
        repository.save(customer2);
        repository.save(customer3);

        Customer given = CustomerUtil.bobby();
        Customer expected = CustomerUtil.bobby(4L);

        Customer answer = repository.save(given);

        assertThat(answer).isEqualTo(expected);
        assertThat(answer.getId()).isEqualTo(expected.getId());
    }
}