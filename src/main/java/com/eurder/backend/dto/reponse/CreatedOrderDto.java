package com.eurder.backend.dto.reponse;

import java.net.URI;
import java.util.Objects;

public class CreatedOrderDto {
    private final Long id;
    private final URI location;
    private final double price;

    public CreatedOrderDto(Long id, URI location, double price) {
        this.id = id;
        this.location = location;
        this.price = price;
    }


    public Long getId() {
        return id;
    }

    public URI getLocation() {
        return location;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreatedOrderDto that)) return false;
        return Double.compare(that.price, price) == 0 && Objects.equals(id, that.id) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location, price);
    }
}
