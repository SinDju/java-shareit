package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.dto.BookingForLastAndNextBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserWithIdDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        userForResponse = UserMapper.toUserWithIdDtoMapper(user);

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

        ownerForResponseDto = UserMapper.toUserWithIdDtoMapper(owner);

        ItemDtoRequest itemDtoRequest1 = ItemDtoRequest.builder().id(1L)
                .name("name item 1").description("desc item 1").available(true).build();

        itemService1.addItem(owner.getId(), itemDtoRequest1);

        item = Item.builder()
                .id(1L)
                .name(itemDtoRequest1.getName())
                .description(itemDtoRequest1.getDescription())
                .owner(owner)
                .available(true)
                .build();

        bookingDto = BookingForItemDto.builder()
                .id(1L)
                .bookerId(userForResponse.getId())
                .start(now.plusDays(1))
                .end(now.plusDays(2))
                .build();

        BookingDtoRequest bookingDto1 = BookingDtoRequest.builder()
                .itemId(item.getId())
                .start(now.plusHours(36))
                .end(now.plusHours(60))
                .build();

        booking = Booking.builder()
                .id(1L)
                .item(item)
                .booker(user)
                .start(bookingDto1.getStart())
                .end(bookingDto1.getEnd())
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

    @Test
    void addToBooking() {
        Booking booking1 = BookingMapper.toBooking(bookingDto777, item, user);

        assertEquals(1L, booking.getId());
        assertEquals(booking1.getItem().getId(), booking.getItem().getId());
        assertEquals(booking1.getItem().getName(), booking.getItem().getName());
        assertEquals(booking1.getBooker().getId(), booking.getBooker().getId());
        assertEquals(booking1.getStatus(), booking.getStatus());
        assertEquals(booking1.getStart(), booking.getStart());
        assertEquals(booking1.getEnd(), booking.getEnd());
    }
}
