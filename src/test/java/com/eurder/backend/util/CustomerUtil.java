package com.eurder.backend.util;

import com.eurder.backend.domain.Address;
import com.eurder.backend.domain.Customer;
import com.eurder.backend.domain.Role;
import com.eurder.backend.dto.reponse.AddressDto;
import com.eurder.backend.dto.reponse.CustomerDto;
import com.eurder.backend.dto.request.CreateCustomerDto;

import java.util.List;

public final class CustomerUtil {
    public static Customer john() {
        return john(null);
    }

    public static Customer john(Long id) {
        return new Customer(id, "John", "Doe", "john@email.local", new Address("Street", "number", "1000", "Brussel"), "0485000000", "password", List.of(new Role("user")));
    }

    public static Customer jack() {
        return jack(null);
    }

    public static Customer jack(Long id) {
        return new Customer(id, "Jack", "Doe", "jack@email.local", new Address("Street", "number", "1000", "Brussel"), "0485000001", "password", List.of(new Role("user")));
    }

    public static Customer joe() {
        return joe(null);
    }

    public static Customer joe(Long id) {
        return new Customer(id, "Joe", "Doe", "joe@email.local", new Address("Street", "number", "1000", "Brussel"), "0485000002", "password", List.of(new Role("user")));
    }

    public static Customer bobby() {
        return bobby(null);
    }

    public static Customer bobby(Long id) {
        return new Customer(id, "Bobby", "Doe", "bobby@email.local", new Address("Street", "number", "1000", "Brussel"), "0485000003", "password", List.of(new Role("user")));
    }

    public static CreateCustomerDto createCustomerDto(Customer customer) {
        AddressDto addressDto = new AddressDto(customer.getAddress().getStreet(), customer.getAddress().getNumber(), customer.getAddress().getZipcode(), customer.getAddress().getCity());
        return new CreateCustomerDto(customer.getFirstName(), customer.getLastName(), customer.getEmail(), addressDto, customer.getPhoneNumber(), "password");
    }

    public static CustomerDto toDto(Customer customer) {
        Address address = customer.getAddress();
        AddressDto addressDto = null;
        if (address != null) {
            addressDto = new AddressDto(address.getStreet(), address.getNumber(), address.getZipcode(), address.getCity());
        }
        return new CustomerDto(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                addressDto,
                customer.getPhoneNumber()
        );
    }
}
