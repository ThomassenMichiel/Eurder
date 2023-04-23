package com.eurder.backend.mapper;


import com.eurder.backend.domain.Customer;
import com.eurder.backend.dto.reponse.CustomerDto;
import com.eurder.backend.dto.request.CreateCustomerDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    private final AddressMapper addressMapper;
    private final PasswordEncoder passwordEncoder;

    public CustomerMapper(AddressMapper addressMapper, PasswordEncoder passwordEncoder) {
        this.addressMapper = addressMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer toDomain(CreateCustomerDto createCustomerDto) {
        return new Customer(createCustomerDto.getFirstName(), createCustomerDto.getLastName(), createCustomerDto.getEmail(), addressMapper.toDomain(createCustomerDto.getAddress()), createCustomerDto.getPhoneNumber(), passwordEncoder.encode(createCustomerDto.getPassword()));
    }

    public CustomerDto toDto(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                addressMapper.toDto(customer.getAddress()),
                customer.getPhoneNumber()
        );
    }
}
