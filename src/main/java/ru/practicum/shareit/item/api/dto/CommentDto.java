package ru.practicum.shareit.item.api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
