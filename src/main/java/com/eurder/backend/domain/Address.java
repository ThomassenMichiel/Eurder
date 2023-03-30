package com.eurder.backend.domain;

import java.util.Objects;

public class Address {
    private final String street;
    private final String number;
    private final String zipcode;
    private final String city;

    public Address(String street, String number, String zipcode, String city) {
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
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) && Objects.equals(number, address.number) && Objects.equals(zipcode, address.zipcode) && Objects.equals(city, address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, number, zipcode, city);
    }
}
