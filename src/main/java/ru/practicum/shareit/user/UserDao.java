package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Optional;

public interface UserDao {
    Collection<User> getAll();

    Optional<User> getUser(long userId);

    User addUser(User user);

    User updateUser(long userId, User user);

    void deleteUser(long userId);
}
