package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemMapperTest {    private final ItemService itemService;
    private final UserService userService;
    private final ItemService itemService1;
    ItemRequest itemRequest1;
    ItemRequest itemRequest2;
    UserDtoRequest ownerDto1;
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

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        nowPlus10min = now.plusMinutes(10);
        nowPlus10hours = now.plusHours(10);

        ownerDto1 = UserDtoRequest.builder()
                .name("name ownerDto1")
                .email("ownerDto1@mans.gf")
                .build();

        owner1 = User.builder()
                .id(ownerDto1.getId())
                .name(ownerDto1.getName())
                .email(ownerDto1.getEmail())
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

        itemSearchOfTextDto = ItemSearchOfTextDto.builder()
                .id(item1.getId())
                .available(true)
                .description(item1.getDescription())
                .name(item1.getName())
                .build();
    }

    @AfterEach
    void tearDown() {
    }
}
