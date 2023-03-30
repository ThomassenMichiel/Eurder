package com.eurder.backend.mapper;


import com.eurder.backend.domain.Address;
import com.eurder.backend.domain.Customer;
import com.eurder.backend.dto.reponse.AddressDto;
import com.eurder.backend.dto.reponse.CustomerDto;
import com.eurder.backend.dto.request.CreateCustomerDto;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public Customer toDomain(CreateCustomerDto createCustomerDto) {
        AddressDto addressDto = createCustomerDto.getAddress();
        Address address = null;
        if (addressDto != null) {
            address = new Address(addressDto.getStreet(), addressDto.getNumber(), addressDto.getZipcode(), addressDto.getCity());
        }
        return new Customer(createCustomerDto.getFirstName(), createCustomerDto.getLastName(), createCustomerDto.getEmail(), address, createCustomerDto.getPhoneNumber(), createCustomerDto.getPassword());
    }

    public CustomerDto toDto(Customer customer) {
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
