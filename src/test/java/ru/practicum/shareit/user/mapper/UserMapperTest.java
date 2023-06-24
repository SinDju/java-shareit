package ru.practicum.shareit.user.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.*;
import ru.practicum.shareit.user.service.*;
import ru.practicum.shareit.item.service.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserMapperTest {
    final UserDtoRequest user1 = new UserDtoRequest(1L, "emai1@mail.com", "testname");
    private final UserService service;
    private final ItemService items;

    @Test
    void updateUserEmailInContext_expectedCorrect_returnUserDtoBeforeUpdate() {
        service.addUser(user1);

        UserDtoRequest updateUser = user1;
        UserDtoResponse userBeforeUpdate = service.updateUser(updateUser.getId(), updateUser);

        assertEquals(updateUser.getEmail(), userBeforeUpdate.getEmail());
        assertEquals(updateUser.getName(), userBeforeUpdate.getName());
        assertEquals(updateUser.getId(), userBeforeUpdate.getId());
    }
}
