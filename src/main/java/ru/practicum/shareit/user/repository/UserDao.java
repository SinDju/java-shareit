package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserDao {
    Collection<User> getAll();

    Optional<User> getUser(long userId);

    User addUser(User user);

    User updateUser(long userId, User user);

    void deleteUser(long userId);
}
