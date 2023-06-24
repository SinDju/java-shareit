package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StateBooking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ObjectBadRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toItemBookingInfoDto;

@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingServiceTest {
    private final BookingService bookingService;
    private final BookingServiceImpl bookingServiceImpl;
    private final UserService userService;
    private final ItemService itemService;

    UserDtoRequest owner;
    UserDtoRequest booker;
    ItemDtoRequest itemDtoToCreate;
    BookingDtoRequest bookingToCreate;

    @BeforeEach
    void setUp() {
        owner = new UserDtoRequest(null, "testUser", "test@email.com");
        booker = new UserDtoRequest(null, "testUser2", "test2@email.com");
        itemDtoToCreate = ItemDtoRequest.builder().name("testItem").description("testDescription").available(true).build();
        bookingToCreate = BookingDtoRequest.builder().itemId(1L).start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2)).build();
    }

    void test(BookingForResponse booking, Status status, UserDtoResponse createdBooker, ItemDtoResponse itemDto) {
        assertThat(booking.getId(), equalTo(1L));
        assertThat(booking.getStart(), equalTo(bookingToCreate.getStart()));
        assertThat(booking.getEnd(), equalTo(bookingToCreate.getEnd()));
        assertThat(booking.getBooker().getId(), equalTo(createdBooker.getId()));
        assertThat(booking.getItem().getId(), equalTo(itemDto.getId()));
        assertThat(booking.getStatus(), equalTo(status));
    }

    @Test
    void createBookingTest() {
        UserDtoResponse createdOwner = userService.addUser(owner);
        UserDtoResponse createdBooker = userService.addUser(booker);
        ItemDtoResponse itemDto = itemService.addItem(createdOwner.getId(), itemDtoToCreate);

        BookingForResponse createdBooking = bookingService.addBooking(createdBooker.getId(), bookingToCreate);

        test(createdBooking, Status.WAITING, createdBooker, itemDto);
    }

    @Test
    void ownerNotTryByBookerTest() {
        UserDtoResponse createdOwner = userService.addUser(owner);
        itemService.addItem(createdOwner.getId(), itemDtoToCreate);
        Exception exception = assertThrows(ObjectNotFoundException.class, ()
                -> bookingService.addBooking(createdOwner.getId(), bookingToCreate));
        assertEquals("Создать бронь на свою вещь нельзя.", exception.getMessage());
    }

    @Test
    void bookerNotAvailableItemTest() {
        UserDtoResponse createdOwner = userService.addUser(owner);
        UserDtoResponse createdBooker = userService.addUser(booker);

        ItemDtoRequest itemDto1 = new ItemDtoRequest(
                1L,
                "name",
                "discript",
                false,
                null
        );
        BookingDtoRequest bookDto = new BookingDtoRequest(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                itemDto1.getId()
        );
        itemService.addItem(createdOwner.getId(), itemDto1);
        Exception exception = assertThrows(ObjectBadRequestException.class, ()
                -> bookingService.addBooking(createdBooker.getId(), bookDto));
        assertEquals("Вещь не доступна для бронирования", exception.getMessage());
    }

    @Test
    void bookerNotAvailableItem2Test() {
        UserDtoResponse createdBooker = userService.addUser(booker);
        BookingDtoRequest bookDto = new BookingDtoRequest(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2),
                2L
        );
        Exception exception = assertThrows(ObjectNotFoundException.class, ()
                -> bookingService.addBooking(createdBooker.getId(), bookDto));
        assertEquals("Вещь с ID 2 не зарегистрирована!", exception.getMessage());
    }

    @Test
    public void testGetStateFromTextTest() {
        assertEquals(StateBooking.ALL, StateBooking.getStateFromText("ALL"));
        assertEquals(StateBooking.CURRENT, StateBooking.getStateFromText("CURRENT"));
        assertEquals(StateBooking.PAST, StateBooking.getStateFromText("PAST"));
        assertEquals(StateBooking.FUTURE, StateBooking.getStateFromText("FUTURE"));
        assertEquals(StateBooking.WAITING, StateBooking.getStateFromText("WAITING"));
        assertEquals(StateBooking.REJECTED, StateBooking.getStateFromText("REJECTED"));
        try {
            StateBooking.getStateFromText("INVALID");
            fail("Expected an RequestFailedException to be thrown");
        } catch (UnsupportedStatusException e) {
            assertEquals("Unknown state: UNSUPPORTED_STATUS", e.getMessage());
        }
    }

    @Test
    public void testGetStateFromText_InvalidTest() {
        String text = "INVALID";
        UnsupportedStatusException exception = assertThrows(UnsupportedStatusException.class, () -> {
            StateBooking.getStateFromText(text);
        });
        assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());
    }

    @Test
    public void testToItemBookingInfoDtoPositiveTest() {
        Booking booking = Booking.builder()
                .id(1L)
                .booker(User.builder().id(2L).build())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        BookingForItemDto itemBookingInfoDto = toItemBookingInfoDto(booking);

        assertEquals(1L, itemBookingInfoDto.getId());
        assertEquals(2L, itemBookingInfoDto.getBookerId());
        assertEquals(booking.getStart(), itemBookingInfoDto.getStart());
        assertEquals(booking.getEnd(), itemBookingInfoDto.getEnd());
    }

    @Test
    public void testToItemBookingInfoDtoNegativeTest() {
        Booking booking = Booking.builder()
                .id(1L)
                .booker(User.builder().id(2L).build())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        BookingForItemDto itemBookingInfoDto = toItemBookingInfoDto(booking);

        assertNotEquals(2L, itemBookingInfoDto.getId());
        assertNotEquals(1L, itemBookingInfoDto.getBookerId());
        assertNotEquals(booking.getStart(), itemBookingInfoDto.getEnd());
        assertNotEquals(booking.getEnd(), itemBookingInfoDto.getStart());
    }

    @Test
    public void approve_withInvalidOwnerId_shouldThrowNotFoundExceptionTest() {
        Long ownerId = 3L;
        Long bookingId = 2L;
        boolean approved = true;

        Exception exception = assertThrows(ObjectNotFoundException.class, () -> bookingService.updateBooking(ownerId, bookingId, approved));

        assertEquals("Пользователь с ID 2 не зарегистрирован!", exception.getMessage());
    }

    @Test
    public void approve_withInvalidBookingId_shouldThrowNotFoundExceptionTest() {
        Long ownerId = 1L;
        Long bookingId = 4L;
        boolean approved = true;

        Exception exception = assertThrows(ObjectNotFoundException.class, () ->
                bookingService.updateBooking(ownerId, bookingId, approved));

        assertEquals("Пользователь с ID 4 не зарегистрирован!", exception.getMessage());
    }

    @AfterEach
    void tearDown() {
    }
}
