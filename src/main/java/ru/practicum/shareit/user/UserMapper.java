package ru.practicum.shareit.user;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto.UserDtoBuilder().name(user.getName()).email(user.getEmail()).build();
    }
}
