package ru.practicum.shareit.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.dto.UserWithIdDto;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class UserMapper {
    public UserWithIdDto toUserWithIdDtoMapper(User user) {
        return new UserWithIdDto(user.getId());
    }

    public User toUserModel(UserDtoRequest userDtoRequest) {
        return new User(
                userDtoRequest.getId(),
                userDtoRequest.getName(),
                userDtoRequest.getEmail()
        );
    }

    public UserDtoRequest toUserDtoRequest(User user) {
        return new UserDtoRequest(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public UserDtoResponse toUserDtoResponse(User user) {
        return new UserDtoResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
