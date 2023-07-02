package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.util.Collection;

public interface UserService {
    Collection<UserDtoResponse> getAllUsers();

    UserDtoResponse getUser(long userId);

    UserDtoResponse addUser(UserDtoRequest userDtoRequest);

    void deleteUser(long userId);

    UserDtoResponse updateUser(long userId, UserDtoRequest userDtoRequest);
}
