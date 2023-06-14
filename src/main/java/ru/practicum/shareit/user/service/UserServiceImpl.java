package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Collection<UserDtoResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDtoResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public UserDtoResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь с ID " +
                        userId + " не зарегистрирован!"));
        return UserMapper.toUserDtoResponse(user);
    }

    @Transactional
    @Override
    public UserDtoResponse addUser(UserDtoRequest userDtoRequest) {
        User user = UserMapper.toUserModel(userDtoRequest);
        User saveUser = userRepository.save(user);
        return UserMapper.toUserDtoResponse(saveUser);
    }

    @Transactional
    @Override
    public UserDtoResponse updateUser(long userId, UserDtoRequest user) {
        User oldUser = userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь с ID " +
                        userId + " не зарегистрирован!"));
        if (user.getName() != null && !user.getName().isBlank()) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            oldUser.setEmail(user.getEmail());
        }
        return UserMapper.toUserDtoResponse(oldUser);
    }

    @Transactional
    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }
}