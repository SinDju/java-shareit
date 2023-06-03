package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Component
public class UserDaoImpl implements UserDao {
    Map<Long, User> users = new HashMap();
    private static long userId = 1;

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public Optional<User> getUser(long userId) {
        if (!users.containsKey(userId)) {
            log.info("Пользователь с ID {} не найден", userId);
            return Optional.empty();
        }
        log.info("Пользователь с ID {} найден", userId);
        return Optional.of(users.get(userId));
    }

    @Override
    public User addUser(User user) {
        user.setId(userId++);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь с ID {}", user.getId());
        return user;
    }

    @Override
    public User updateUser(long userId, User user) {
        if (users.containsKey(userId)) {
            User updateUser = users.get(userId);
            if (user.getName() != null) {
                updateUser.setName(user.getName());
            }
            if (user.getEmail() != null) {
                updateUser.setEmail(user.getEmail());
            }
        }
        log.info("Обновлен пользователь с ID {}", userId);
        return users.get(userId);
    }

    @Override
    public void deleteUser(long userId) {
        users.remove(userId);
        log.info("Удален пользователь с ID {}", userId);
    }
}