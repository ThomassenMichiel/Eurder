package com.eurder.backend.dto.reponse;

import java.net.URI;

public class CreatedObjectIdDto {
    private final Long id;
    private final URI location;

    public CreatedObjectIdDto(Long id, URI location) {
        this.id = id;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public URI getLocation() {
        return location;
    }
}
