package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAllUsers();

    UserDto getUser(long userId);

    UserDto addUser(UserDto userDto);

    void deleteUser(long userId);

    UserDto updateUser(long userId, UserDto userDto);
}
