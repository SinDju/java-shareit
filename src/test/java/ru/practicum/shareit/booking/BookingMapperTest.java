package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.mapper.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.dto.BookingForLastAndNextBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserWithIdDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingMapperTest {
    private BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService1;
    User user;
    UserWithIdDto userForResponse;
    User owner;
    UserWithIdDto ownerForResponseDto;
    Item item;
    ItemWithBookingDto itemWithBookingDto;
    BookingForLastAndNextBookingDto bookingForLastAndNextBookingDto;
    BookingForItemDto bookingForItemDto;
    BookingForItemDto bookingDto;
    Booking booking;
    BookingDtoRequest bookingDto777;
    Booking booking777;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        user = User.builder()
                .id(1L)
                .name("name user 1")
                .email("user1@ugvg@rsdx")
                .build();

        userForResponse = UserMapper.toUserWithIdDtoMapper(user);

        owner = User.builder()
                .id(2L)
                .name("name owner 2")
                .email("owner@jjgv.zw")
                .build();

        ownerForResponseDto = UserMapper.toUserWithIdDtoMapper(owner);

        item = Item.builder()
                .id(1L)
                .name("name item 1")
                .description("desc item 1")
                .owner(owner)
                .available(true)
                .build();

        bookingDto = BookingForItemDto.builder()
                .id(1L)
                .bookerId(userForResponse.getId())
                .start(now.plusDays(1))
                .end(now.plusDays(2))
                .build();

        booking = Booking.builder()
                .id(bookingDto.getId())
                .item(item)
                .booker(user)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .build();

        bookingDto777 = BookingDtoRequest.builder()
                .itemId(item.getId())
                .start(now.plusHours(36))
                .end(now.plusHours(60))
                .build();

        booking777 = Booking.builder()
                .item(item)
                .booker(user)
                .start(bookingDto777.getStart())
                .end(bookingDto777.getEnd())
                .build();

        itemWithBookingDto = ItemWithBookingDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build();

        bookingForLastAndNextBookingDto = BookingForLastAndNextBookingDto.builder()
                .id(booking.getId())
                .bookerId(user.getId())
                .build();

        bookingForItemDto = BookingMapper.toItemBookingInfoDto(booking);
    }

    @AfterEach
    void tearDown() {
    }
}
