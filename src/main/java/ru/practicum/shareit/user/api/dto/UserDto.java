package ru.practicum.shareit.user.api.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UserDto {
    private long id;
    private String name;
    private String email;
}
