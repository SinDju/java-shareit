package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.*;
import ru.practicum.shareit.user.service.UserService;

@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceIntegTest {
    private final ItemRequestService requestService;
    private final UserService userService;

    @BeforeEach
    public void setUp() {
        UserDtoRequest userDto = new UserDtoRequest(1L,
                "mail@gmail.com",
                "name"
        );
        userService.addUser(userDto);
    }

    @Test
    public void shouldSuccessAddRequest() {
        ItemRequestDto itemRequest = new ItemRequestDto("use practicum");

        ItemRequestResponseDto newItemRequest = requestService.addItemRequest(1, itemRequest);

        Assertions.assertNotNull(newItemRequest);
        Assertions.assertEquals(newItemRequest.getDescription(), itemRequest.getDescription());
    }
}
