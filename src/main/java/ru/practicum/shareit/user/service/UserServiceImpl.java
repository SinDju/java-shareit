package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь с ID " +
                        userId + " не зарегистрирован!"));
        return user;
    }

    @Transactional
    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUser(long userId, User user) {
        List<User> userList = getAllUsers().stream().collect(Collectors.toList());
        userList.remove(getUser(userId));
        for (User user1 : userList) {
            if (user1.getEmail().equals(user.getEmail())) {
                throw new DuplicateException();
            }
        }
        User updateUser = userRepository.getReferenceById(userId);
        if (userRepository.existsById(userId)) {
            if (user.getName() != null) {
                updateUser.setName(user.getName());
            }
            if (user.getEmail() != null) {
                updateUser.setEmail(user.getEmail());
            }
        }
        return userRepository.save(updateUser);
    }

    @Transactional
    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }
}