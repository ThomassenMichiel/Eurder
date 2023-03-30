package com.eurder.backend.controller;

import com.eurder.backend.dto.reponse.CreatedOrderDto;
import com.eurder.backend.dto.request.CreateOrderDto;
import com.eurder.backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    public ResponseEntity<CreatedOrderDto> save(@Valid @RequestBody CreateOrderDto createOrderDto) {
        CreatedOrderDto createdOrderDto = service.save(createOrderDto);
        return ResponseEntity.created(createdOrderDto.getLocation())
                .body(createdOrderDto);
    }
}
