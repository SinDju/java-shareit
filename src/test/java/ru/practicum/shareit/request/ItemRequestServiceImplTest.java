package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ItemRequestServiceImplTest {
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

        assertNotNull(newItemRequest);
        assertEquals(newItemRequest.getDescription(), itemRequest.getDescription());
    }
}
