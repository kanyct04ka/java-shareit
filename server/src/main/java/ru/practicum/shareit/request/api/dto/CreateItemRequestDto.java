package ru.practicum.shareit.request.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CreateItemRequestDto {
    @NotBlank
    private String description;
}
