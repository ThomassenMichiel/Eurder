package com.eurder.backend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateOrderDto {
    @NotNull(message = "ItemGroupList cannot be null")
    @NotEmpty(message = "ItemGroupList cannot be empty")
    private List<CreateItemGroupDto> itemGroupList;

    public CreateOrderDto() {
    }

    public CreateOrderDto(List<CreateItemGroupDto> itemGroupDtoList) {
        this.itemGroupList = itemGroupDtoList;
    }

    public List<CreateItemGroupDto> getItemGroupList() {
        return itemGroupList;
    }
}
