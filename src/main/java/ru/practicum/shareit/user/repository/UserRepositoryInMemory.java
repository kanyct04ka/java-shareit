package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Component
public class UserRepositoryInMemory implements UserRepository {

    private final Map<Long, User> users;
    private long count;

    public UserRepositoryInMemory() {
        users = new HashMap<>();
        count = 0;
    }

    private long getNextUserId() {
        return ++count;
    }

    @Override
    public User saveUser(User user) {
        user.setId(getNextUserId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getUser(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> getUser(String email) {
        return users.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void removeUser(long id) {
        users.remove(id);
    }


}
