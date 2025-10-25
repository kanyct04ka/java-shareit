package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.api.dto.CreateUserDto;
import ru.practicum.shareit.user.api.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapToUser(CreateUserDto createUserDto);

    UserDto mapToUserDto(User user);

}
