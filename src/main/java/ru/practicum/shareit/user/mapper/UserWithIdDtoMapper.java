package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserWithIdDto;
import ru.practicum.shareit.user.model.User;

public class UserWithIdDtoMapper {
    public static UserWithIdDto toUserWithIdDtoMapper(User user) {
        return new UserWithIdDto(user.getId());
    }
}
