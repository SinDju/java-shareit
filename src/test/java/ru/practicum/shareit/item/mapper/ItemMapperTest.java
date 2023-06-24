package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ObjectForbiddenException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserForItemRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemMapperTest {
    private final ItemService itemService;
    private final UserService userService;
    ItemRequest itemRequest1;
    ItemRequest itemRequest2;
    UserDtoRequest ownerDto1;
    User user;
    User owner;
    User owner1;
    UserForItemRequestDto requesterDto101;
    User requester101;
    UserDtoRequest bookerDto;
    User booker;
    UserDtoRequest userDtoForTest;
    User userForTest;
    LocalDateTime now;
    LocalDateTime nowPlus10min;
    LocalDateTime nowPlus10hours;
    Item item1;
    ItemRequestDto itemDto1;
    ItemRequestDto itemRequestDto1;
    ItemSearchOfTextDto itemSearchOfTextDto;
    ItemForItemRequestResponseDto itemForItemRequestResponseDto;
    ItemWithBookingDto itemWithBookingDto;
    Booking booking1;
    BookingDtoRequest bookingDto1;
    CommentDtoRequest commentDto;
    ItemDtoRequest itemDtoRequest1;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        UserDtoRequest userDto = new UserDtoRequest(1L,
                "name",
                "mail@gmail.com"
        );
        userService.addUser(userDto);

        user = User.builder()
                .id(1L)
                .name("name")
                .email("mail@gmail.com")
                .build();


        UserDtoRequest userDto2 = new UserDtoRequest(2L,
                "name owner 2",
                "owner@jjgv.zw"
        );

        userService.addUser(userDto2);

        owner = User.builder()
                .id(2L)
                .name("name owner 2")
                .email("owner@jjgv.zw")
                .build();

        requesterDto101 = UserForItemRequestDto.builder()
                .name("name requesterDto101")
                .build();

        requester101 = User.builder()
                .id(requesterDto101.getId())
                .name(requesterDto101.getName())
                .email("requesterDto101@mans.gf")
                .build();

        userDtoForTest = UserDtoRequest.builder()
                .name("name userDtoForTest")
                .email("userDtoForTest@userDtoForTest.zx")
                .build();

        userForTest = User.builder()
                .name(userDtoForTest.getName())
                .email(userDtoForTest.getEmail())
                .build();

        bookerDto = UserDtoRequest.builder()
                .name("booker")
                .email("booker@wa.dzd")
                .build();

        booker = User.builder()
                .name(bookerDto.getName())
                .email(bookerDto.getEmail())
                .build();

        itemRequest1 = ItemRequest.builder()
                .description("description for request 1")
                .requester(requester101)
                .created(now)
                .build();
        itemDtoRequest1 = ItemDtoRequest.builder()
                .id(1L)
                .name("name for item 1")
                .description("description for item 1")
                .available(true)
                .build();

        item1 = Item.builder()
                .id(1L)
                .name("name for item 1")
                .description("description for item 1")
                .owner(owner1)
                .available(true)
                .request(itemRequest1)
                .build();

        itemDto1 = ItemRequestDto.builder()
                .description(item1.getDescription())
                .build();

        itemRequestDto1 = ItemRequestDto.builder()
                .description(item1.getDescription())
                .build();

        ItemDtoRequest itemDtoRequest = ItemDtoRequest.builder()
                .id(2L)
                .name("book")
                .description("read book")
                .available(true)
                .build();

        Item item = ItemMapper.toItem(itemDtoRequest);

        commentDto = CommentDtoRequest.builder()
                .text("comment 1")
                .build();

        itemForItemRequestResponseDto = ItemForItemRequestResponseDto.builder()
                .id(item1.getId())
                .available(true)
                .requestId(itemRequest1.getId())
                .name(item1.getName())
                .description(item1.getDescription())
                .build();

        itemWithBookingDto = ItemWithBookingDto.builder()
                .id(item1.getId())
                .name(item1.getName())
                .build();

        itemSearchOfTextDto = ItemMapper.toItemSearchOfTextDto(item);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void addItem() {
        ItemDtoResponse itemDtoResponse = itemService.addItem(user.getId(), itemDtoRequest1);
        ItemDtoRequest itemDtoRequest2 = itemDtoRequest1;

        assertThrows(ObjectForbiddenException.class, () -> itemService.updateItem(user.getId(), itemDtoRequest2.getId(), itemDtoRequest2));
    }
        /*ItemDtoRequest itemDtoRequest2 = itemDtoRequest1;
        ItemDtoResponse itemDtoResponse2 = itemService.updateItem(user.getId(), itemDtoRequest2.getId(), itemDtoRequest2);
        assertEquals(itemDtoResponse.getAvailable(), itemDtoResponse2.getAvailable());
        assertEquals(itemDtoResponse.getDescription(), itemDtoResponse2.getDescription());
        assertEquals(itemDtoResponse.getName(), itemDtoResponse2.getName());
        assertEquals(itemDtoResponse.getId(), itemDtoResponse2.getId());
    }*/
}
