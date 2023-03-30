package com.eurder.backend.dto.reponse;

import java.net.URI;

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
}
