package com.eurder.backend.mapper;

import com.eurder.backend.domain.Address;
import com.eurder.backend.dto.reponse.AddressDto;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDto toDto(Address address) {
        return new AddressDto(address.getStreet(), address.getNumber(), address.getZipcode(), address.getCity());
    }

    public Address toDomain(AddressDto addressDto) {
        return new Address(addressDto.getStreet(), addressDto.getNumber(), addressDto.getZipcode(), addressDto.getCity());
    }
}
