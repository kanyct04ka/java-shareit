package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictDataException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.api.UserDtoMapper;
import ru.practicum.shareit.user.api.dto.CreateUserDto;
import ru.practicum.shareit.user.api.dto.UpdateUserDto;
import ru.practicum.shareit.user.api.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(CreateUserDto createUser) {
        // email should be unique
        if (userRepository.getUser(createUser.getEmail()).isPresent()) {
            String error = "user's email already used by another user";
            log.warn(error);
            throw new ConflictDataException(error);
        }

        User user = userRepository.saveUser(UserDtoMapper.mapToUser(createUser));
        return UserDtoMapper.mapToUserDto(user);
    }

    @Override
    public UserDto getUser(long id) {
        User user = userRepository.getUser(id)
                .orElseThrow(() -> new EntityNotFoundException("user with requested id not found"));

        return UserDtoMapper.mapToUserDto(user);
    }

    @Override
    public UserDto updateUser(long id, UpdateUserDto updateUserInfo) {
        User user = userRepository.getUser(id)
                .orElseThrow(() -> new EntityNotFoundException("user with requested id not found"));

        if (updateUserInfo.getName() != null) {
            user.setName(updateUserInfo.getName());
        }

        if (updateUserInfo.getEmail() != null) {
            Optional<User> userWithTheSameEmail = userRepository.getUser(updateUserInfo.getEmail());
            if (userWithTheSameEmail.isPresent() && !userWithTheSameEmail.get().equals(user)) {
                String error = "user's email already used by another user";
                log.warn(error);
                throw new ConflictDataException(error);
            }

            user.setEmail(updateUserInfo.getEmail());
        }

        userRepository.updateUser(user);
        return UserDtoMapper.mapToUserDto(user);
    }

    @Override
    public void deleteUser(long id) {
        // checking for existing user is unnecessary for deleting
        userRepository.removeUser(id);
    }

    @Override
    public boolean userExists(long id) {
        return userRepository.getUser(id).isPresent();
    }

}
