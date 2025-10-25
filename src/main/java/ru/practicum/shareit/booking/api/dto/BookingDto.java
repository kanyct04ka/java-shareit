package ru.practicum.shareit.booking.api.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import java.time.LocalDateTime;


@Data
public class BookingDto {
    private long id;
    private BookingItemDto item;
    private BookingUserDto booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;

}
