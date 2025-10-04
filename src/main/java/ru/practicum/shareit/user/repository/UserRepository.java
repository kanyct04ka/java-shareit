package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;
import java.util.Optional;


public interface UserRepository {
    User saveUser(User user);

    Optional<User> getUser(long id);

    Optional<User> getUser(String email);

    void updateUser(User user);

    void removeUser(long id);
}
