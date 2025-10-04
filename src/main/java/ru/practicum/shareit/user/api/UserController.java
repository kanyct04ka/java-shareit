package ru.practicum.shareit.user.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.api.dto.CreateUserDto;
import ru.practicum.shareit.user.api.dto.UpdateUserDto;
import ru.practicum.shareit.user.api.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;


@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(
            @RequestBody @Valid
            CreateUserDto user
    ) {
        log.info("Request to create user with data: {}", user.toString());
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public UserDto getUser(
            @PathVariable @Positive(message = "id should be positive number")
            long id
    ) {
        return userService.getUser(id);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(
            @PathVariable @Positive(message = "id should be positive number")
            long id,
            @RequestBody @Valid
            UpdateUserDto updateUserDto
    ) {
        log.info("Request to update user with id: {}", id);
        return userService.updateUser(id, updateUserDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @PathVariable @Positive(message = "id should be positive number")
            long id
    ) {
        log.info("Request to delete user with id: {}", id);
        userService.deleteUser(id);
    }
}
