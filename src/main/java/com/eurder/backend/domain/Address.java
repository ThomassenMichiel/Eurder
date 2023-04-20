package com.eurder.backend.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "street")
    private String street;
    @Column(name = "number")
    private String number;
    @Column(name = "zipcode")
    private String zipcode;
    @Column(name = "city")
    private String city;

    public Address() {
    }

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
