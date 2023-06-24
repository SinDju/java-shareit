package ru.practicum.shareit.request.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestMapperTest {
    final User user1 = new User(1L, "name", "mail@gmail.com");
    final ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .description("description for request 1")
            .build();
    private final UserService userService;
    private final ItemRequestService itemRequestService;

    @BeforeEach
    public void setUp() {
        UserDtoRequest userDto = new UserDtoRequest(1L,
                "name",
                "mail@gmail.com"
        );
        userService.addUser(userDto);
    }

    @Test
    void addToItemRequest() {
        ItemRequestResponseDto itemRequestResponseDto =  itemRequestService.addItemRequest(user1.getId(), itemRequestDto);
        ItemRequest itemRequest = ItemRequestDtoMapper.toItemRequest(itemRequestDto, user1);

        assertNotNull(itemRequest);
        assertEquals(itemRequestDto.getDescription(), itemRequestResponseDto.getDescription());
        assertEquals(user1.getId(), itemRequestResponseDto.getRequester().getId());
        assertEquals(user1.getName(), itemRequestResponseDto.getRequester().getName());
        assertEquals(1L, itemRequestResponseDto.getId());
    }
}
