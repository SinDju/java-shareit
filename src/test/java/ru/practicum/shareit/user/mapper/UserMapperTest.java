package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserMapperTest {

    @Test
    void toUserWithIdDtoMapper() {
        var original = User.builder()
                .id(1L)
                .name("name")
                .email("mail@gmail.com")
                .build();
        var result = UserMapper.toUserWithIdDtoMapper(original);

        assertNotNull(result);
        assertEquals(original.getId(), result.getId());
    }

    @Test
    void toUserModel() {
        var original = new UserDtoRequest(1L, "Sakura", "sakura@mail.ru");

        var result = UserMapper.toUserModel(original);

        assertNotNull(result);
        assertEquals(original.getId(), result.getId());
        assertEquals(original.getName(), result.getName());
        assertEquals(original.getEmail(), result.getEmail());
    }
}