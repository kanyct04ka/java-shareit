package ru.practicum.shareit.user.api.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;


@Data
public class UpdateUserDto {
    private String name;
    @Email
    private String email;
}
