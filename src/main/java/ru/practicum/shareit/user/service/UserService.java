package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    Collection<User> getAllUsers();

    User getUser(long userId);

    User addUser(User user);

    void deleteUser(long userId);

    User updateUser(long userId, User user);
}
