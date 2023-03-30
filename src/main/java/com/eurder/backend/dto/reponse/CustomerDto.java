package com.eurder.backend.dto.reponse;

import java.util.Objects;

public class CustomerDto {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final AddressDto address;
    private final String phoneNumber;

    public CustomerDto(Long id, String firstName, String lastName, String email, AddressDto address, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public AddressDto getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerDto that)) return false;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(address, that.address) && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, address, phoneNumber);
    }
}
