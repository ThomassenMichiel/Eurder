package com.eurder.backend.controller;

import com.eurder.backend.dto.reponse.CreatedObjectIdDto;
import com.eurder.backend.dto.request.CreateItemDto;
import com.eurder.backend.dto.request.UpdateItemDto;
import com.eurder.backend.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<CreatedObjectIdDto> save(@Valid @RequestBody CreateItemDto createItemDto) {
        Long createdItem = service.save(createItemDto);
        CreatedObjectIdDto createdObjectIdDto = new CreatedObjectIdDto(createdItem, URI.create("/items/" + createdItem));
        return ResponseEntity.created(createdObjectIdDto.getLocation()).body(createdObjectIdDto);
    }

    @PutMapping()
    @ResponseStatus(OK)
    public ResponseEntity<Void> update(@Valid @RequestBody UpdateItemDto updateItemDto) {
        service.update(updateItemDto);
        return ResponseEntity.ok().build();
    }
}
