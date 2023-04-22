package com.eurder.backend.controller;

import com.eurder.backend.dto.reponse.CreatedOrderDto;
import com.eurder.backend.dto.reponse.ItemsToShipListDto;
import com.eurder.backend.dto.reponse.OrderListDto;
import com.eurder.backend.dto.request.CreateOrderDto;
import com.eurder.backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

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

    @GetMapping
    @ResponseStatus(OK)
    public ResponseEntity<OrderListDto> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping("/reorder/{previousOrderId}")
    @ResponseStatus(OK)
    public ResponseEntity<CreatedOrderDto> reorder(@PathVariable(name = "previousOrderId") Long previousOrderId) {
        CreatedOrderDto reorder = service.reorder(previousOrderId);
        return ResponseEntity.created(reorder.getLocation()).body(reorder);
    }

    @GetMapping("/shipping")
    @ResponseStatus(OK)
    public ResponseEntity<ItemsToShipListDto> findAllShippingToday() {
        return ResponseEntity.ok(service.findAllItemsToShipToday());
    }
}
