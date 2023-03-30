package com.eurder.backend.dto.reponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Validated
public class AddressDto {
    @NotNull(message = "Street cannot be null")
    @NotBlank(message = "Street cannot be empty")
    private final String street;
    @NotNull(message = "Number cannot be null")
    @NotBlank(message = "Number cannot be empty")
    private final String number;
    @NotNull(message = "Zipcode cannot be null")
    @NotBlank(message = "Zipcode cannot be empty")
    private final String zipcode;
    @NotNull(message = "City cannot be null")
    @NotBlank(message = "City cannot be empty")
    private final String city;

    public AddressDto(String street, String number, String zipcode, String city) {
        this.street = street;
        this.number = number;
        this.zipcode = zipcode;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getCity() {
        return city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressDto that)) return false;
        return Objects.equals(street, that.street) && Objects.equals(number, that.number) && Objects.equals(zipcode, that.zipcode) && Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, number, zipcode, city);
    }
}
