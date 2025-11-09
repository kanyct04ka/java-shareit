package ru.practicum.shareit.item.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemBookingDateDto {
    private LocalDateTime start;
    private LocalDateTime end;
}
