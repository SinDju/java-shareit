package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    private final UserRepository repository;

    @Override
    public Collection<User> getAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Optional<User> getUser(long userId) {
        if (!repository.existsById(userId)) {
            log.info("Пользователь с ID {} не найден", userId);
            return Optional.empty();
        }
        log.info("Пользователь с ID {} найден", userId);
        return Optional.of(repository.findById(userId).get());
    }

    @Override
    public User addUser(User user) {
        repository.save(user);
        log.info("Добавлен пользователь с ID {}", user.getId());
        return user;
    }

    @Override
    @Transactional
    public User updateUser(long userId, User user) {
        User updateUser = repository.getReferenceById(userId);
        if (repository.existsById(userId)) {
            if (user.getName() != null) {
                updateUser.setName(user.getName());
            }
            if (user.getEmail() != null) {
                updateUser.setEmail(user.getEmail());
            }
        }
        repository.save(updateUser);
        log.info("Обновлен пользователь с ID {}", userId);
        return repository.getReferenceById(userId);
    }

    @Override
    public void deleteUser(long userId) {
        repository.deleteById(userId);
        log.info("Удален пользователь с ID {}", userId);
    }
}
