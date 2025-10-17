package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictDataException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.api.dto.CreateUserDto;
import ru.practicum.shareit.user.api.dto.UpdateUserDto;
import ru.practicum.shareit.user.api.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(CreateUserDto createUser) {
        // email should be unique
        if (userRepository.findByEmail(createUser.getEmail()).isPresent()) {
            String error = "user's email already used by another user";
            log.warn(error);
            throw new ConflictDataException(error);
        }

        User user = userMapper.mapToUser(createUser);
        user.setCreated(Instant.now());
        user.setUpdated(Instant.now());
        return userMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
    public UserDto getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with requested id not found"));

        return userMapper.mapToUserDto(user);
    }

    @Override
    public UserDto updateUser(long id, UpdateUserDto updateUserInfo) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with requested id not found"));

        if (updateUserInfo.getName() != null) {
            user.setName(updateUserInfo.getName());
        }

        if (updateUserInfo.getEmail() != null) {
            Optional<User> userWithTheSameEmail = userRepository.findByEmail(updateUserInfo.getEmail());
            if (userWithTheSameEmail.isPresent() && !userWithTheSameEmail.get().equals(user)) {
                String error = "user's email already used by another user";
                log.warn(error);
                throw new ConflictDataException(error);
            }

            user.setEmail(updateUserInfo.getEmail());
        }

        user.setUpdated(Instant.now());
        userRepository.save(user);
        return userMapper.mapToUserDto(user);
    }

    @Override
    public void deleteUser(long id) {
        // checking for existing user is unnecessary for deleting
        userRepository.deleteById(id);
    }

    @Override
    public boolean userExists(long id) {
        return userRepository.findById(id).isPresent();
    }

}
