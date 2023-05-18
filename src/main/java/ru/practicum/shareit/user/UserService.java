package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserService {
    Collection<User> getAllUsers();

    User getUser(long userId);

    User addUser(User user);

    void deleteUser(long userId);

    User updateUser(long userId, User user);
}
