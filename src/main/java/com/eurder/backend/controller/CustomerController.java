package com.eurder.backend.controller;

import com.eurder.backend.dto.reponse.CreatedObjectIdDto;
import com.eurder.backend.dto.reponse.CustomerDto;
import com.eurder.backend.dto.reponse.CustomerListDto;
import com.eurder.backend.dto.request.CreateCustomerDto;
import com.eurder.backend.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    public ResponseEntity<CreatedObjectIdDto> save(@Valid @RequestBody CreateCustomerDto createCustomerDto) {
        Long savedId = service.save(createCustomerDto);
        CreatedObjectIdDto createdObjectIdDto = new CreatedObjectIdDto(savedId, URI.create("/customers/" + savedId));
        return ResponseEntity.created(createdObjectIdDto.getLocation())
                .body(createdObjectIdDto);
    }

    @GetMapping()
    public ResponseEntity<CustomerListDto> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> findById(@PathVariable @NotNull @Min(0) Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
