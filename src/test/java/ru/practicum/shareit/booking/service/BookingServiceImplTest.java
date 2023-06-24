package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.ObjectBadRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BookingServiceImplTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final BookingServiceImpl bookingServiceImpl;
    private UserDtoResponse testUser;
    private UserDtoResponse secondTestUser;
    private ItemDtoResponse itemDtoFromDB;
    private BookingDtoRequest bookItemRequestDto;
    private BookingDtoRequest secondBookItemRequestDto;

    @BeforeEach
    public void setUp() {
        ItemDtoRequest itemDto = ItemDtoRequest.builder()
                .name("Poke Ball")
                .description("The Poke Ball is a sphere")
                .available(true)
                .build();

        UserDtoRequest userDto = UserDtoRequest.builder()
                .name("Ash")
                .email("ash@gmail.com")
                .build();

        UserDtoRequest secondUserDto = UserDtoRequest.builder()
                .name("Misty")
                .email("misty@gmail.com")
                .build();

        testUser = userService.addUser(userDto);
        secondTestUser = userService.addUser(secondUserDto);
        itemDtoFromDB = itemService.addItem(testUser.getId(), itemDto);

        bookItemRequestDto = BookingDtoRequest.builder()
                .start(LocalDateTime.now().plusNanos(1))
                .end(LocalDateTime.now().plusNanos(2))
                .itemId(itemDtoFromDB.getId())
                .build();
        secondBookItemRequestDto = BookingDtoRequest.builder()
                .start(LocalDateTime.now().plusHours(5))
                .end(LocalDateTime.now().plusHours(10))
                .itemId(itemDtoFromDB.getId())
                .build();
/*
        itemRepositoryJpa = mock(ItemRepositoryJpa.class);
        userRepositoryJpa = mock(UserRepositoryJpa.class);
        bookingRepositoryJpa = mock(BookingRepositoryJpa.class);
        itemRepository = mock(ItemRepositoryJpa.class);
        bookingService = new BookingServiceImpl(bookingRepositoryJpa, itemRepository, userRepository, bookingMapper,
                bookingForResponseBookingDtoMapper);
*/
    }

    @Test
    void createBookingTest() {
        BookingForResponse addBooking = bookingService.addBooking(secondTestUser.getId(), bookItemRequestDto);

        assertNotNull(addBooking.getId());
    }

    @Test
    void updateBookingTest() {
        BookingForResponse bookingDtoFromDB = bookingService.addBooking(secondTestUser.getId(), bookItemRequestDto);
        BookingForResponse approveBooking = bookingService.updateBooking(testUser.getId(), bookingDtoFromDB.getId(),
                true);

        assertEquals(approveBooking.getStatus(), Status.APPROVED);
    }

    @Test
    void getBookingByIdTest() {
        BookingForResponse bookingDtoFromDB = bookingService.addBooking(secondTestUser.getId(), bookItemRequestDto);
        BookingForResponse approveBooking = bookingService.updateBooking(testUser.getId(), bookingDtoFromDB.getId(), true);

        assertThrows(ObjectNotFoundException.class,
                () -> bookingService.getBooking(999L, approveBooking.getId()));
    }

    @Test
    void getAllBookingsTest() {
        BookingForResponse bookingDtoFromDB = bookingService.addBooking(secondTestUser.getId(), bookItemRequestDto);
        BookingForResponse bookingDtoFromDB2 = bookingService.addBooking(secondTestUser.getId(), secondBookItemRequestDto);
        List<BookingForResponse> bookingDtos = List.of(bookingDtoFromDB, bookingDtoFromDB2);
        List<BookingForResponse> bookings = bookingService.getAllBookingByUser("ALL",
                secondTestUser.getId(), 0, 3);

        assertEquals(bookings.size(), bookingDtos.size());
    }

    @Test
    void getAllOwnerBookingsTest() {
        BookingForResponse bookingDtoFromDB = bookingService.addBooking(secondTestUser.getId(), bookItemRequestDto);
        BookingForResponse bookingDtoFromDB2 = bookingService.addBooking(secondTestUser.getId(), secondBookItemRequestDto);
        List<BookingForResponse> bookingDtos = List.of(bookingDtoFromDB, bookingDtoFromDB2);
        List<BookingForResponse> bookings = bookingService
                .getAllBookingByOwner("ALL", testUser.getId(), 0, 3);

        assertEquals(bookings.size(), bookingDtos.size());
    }

    @Test
    void approveBookingWrongOwnerTest() {
        BookingForResponse bookingDtoFromDB = bookingService.addBooking(secondTestUser.getId(), bookItemRequestDto);

        assertThrows(ObjectNotFoundException.class,
                () -> bookingService.updateBooking(secondTestUser.getId(), bookingDtoFromDB.getId(), true));
    }

    @Test
    void getAllBookingsNonExistentStateTest() {
        String nonExistentState = "nonExistentState";
        bookingService.addBooking(secondTestUser.getId(), bookItemRequestDto);

        assertThrows(UnsupportedStatusException.class,
                () -> bookingService.getAllBookingByOwner(nonExistentState, secondTestUser.getId(), 0, 3));
    }

    @Test
    void getAllOwnerBookingsRejectedStateTest() {
        BookingForResponse bookingDtoFromDB = bookingService.addBooking(secondTestUser.getId(), bookItemRequestDto);
        BookingForResponse bookingDtoFromDB2 = bookingService.updateBooking(bookingDtoFromDB.getId(),
                testUser.getId(), false);
        List<BookingForResponse> bookings = bookingService
                .getAllBookingByOwner("REJECTED", testUser.getId(), 0, 3);

        assertEquals(bookings.size(), 1);
        assertEquals(bookings.get(0).getStatus(), Status.REJECTED);
    }

    @Test
    void getAllBookingsCurrentStateTest() {
        BookingDtoRequest bookingDto = BookingDtoRequest.builder()
                .start(LocalDateTime.now().minusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .itemId(itemDtoFromDB.getId())
                .build();
        List<BookingDtoRequest> bookingDtos = List.of(bookingDto);
        BookingForResponse firstBooking = bookingService.addBooking(secondTestUser.getId(), bookingDto);
        bookingService.updateBooking(testUser.getId(), firstBooking.getId(), true);
        List<BookingForResponse> currentBookings = bookingService.getAllBookingByUser("CURRENT",
                secondTestUser.getId(), 0, 3);
        BookingForResponse currentBooking = currentBookings.get(0);

        assertEquals(currentBookings.size(), bookingDtos.size());
    }

    @Test
    void getAllBookingsFutureStateTest() {
        BookingDtoRequest bookingDto = BookingDtoRequest.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .itemId(itemDtoFromDB.getId())
                .build();
        List<BookingDtoRequest> bookingDtos = List.of(bookingDto);
        BookingForResponse firstBooking = bookingService.addBooking(secondTestUser.getId(), bookingDto);
        List<BookingForResponse> futureBookings = bookingService.getAllBookingByUser("FUTURE",
                secondTestUser.getId(), 0, 3);
        BookingForResponse futureBooking = futureBookings.get(0);

        assertEquals(futureBookings.size(), bookingDtos.size());
        assertEquals(futureBooking.getId(), firstBooking.getId());
    }

    @Test
    void getAllBookingsPastStateTest() {
        BookingDtoRequest bookingDto = BookingDtoRequest.builder()
                .start(LocalDateTime.now().minusHours(2))
                .end(LocalDateTime.now().minusHours(1))
                .itemId(itemDtoFromDB.getId())
                .build();
        List<BookingDtoRequest> bookingDtos = List.of(bookingDto);
        BookingForResponse firstBooking = bookingService.addBooking(secondTestUser.getId(), bookingDto);
        bookingService.updateBooking(testUser.getId(), firstBooking.getId(), true);
        List<BookingForResponse> pastBookings = bookingService.getAllBookingByUser("PAST",
                secondTestUser.getId(), 0, 3);
        BookingForResponse pastBooking = pastBookings.get(0);

        assertEquals(pastBookings.size(), bookingDtos.size());
        assertEquals(pastBooking.getId(), firstBooking.getId());
    }

    @Test
    void getAllOwnerBookingsFutureStateTest() {
        BookingDtoRequest bookingDto = BookingDtoRequest.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .itemId(itemDtoFromDB.getId())
                .build();
        List<BookingDtoRequest> bookingDtos = List.of(bookingDto);
        BookingForResponse firstBooking = bookingService.addBooking(secondTestUser.getId(), bookingDto);
        bookingService.updateBooking(testUser.getId(), firstBooking.getId(), true);
        List<BookingForResponse> futureBookings = bookingService.getAllBookingByOwner("FUTURE",
                testUser.getId(), 0, 3);
        BookingForResponse futureBooking = futureBookings.get(0);

        assertEquals(futureBookings.size(), bookingDtos.size());
        assertEquals(futureBooking.getId(), firstBooking.getId());
    }

    @Test
    void getAllOwnerBookingsPastStateTest() {
        BookingDtoRequest bookingDto = BookingDtoRequest.builder()
                .start(LocalDateTime.now().minusHours(2))
                .end(LocalDateTime.now().minusHours(1))
                .itemId(itemDtoFromDB.getId())
                .build();
        List<BookingDtoRequest> bookingDtos = List.of(bookingDto);
        BookingForResponse firstBooking = bookingService.addBooking(secondTestUser.getId(), bookingDto);
        bookingService.updateBooking(testUser.getId(), firstBooking.getId(), true);

        List<BookingForResponse> pastBookings = bookingService.getAllBookingByOwner("PAST",
                testUser.getId(), 0, 3);
        BookingForResponse pastBooking = pastBookings.get(0);

        assertEquals(pastBookings.size(), bookingDtos.size());
        assertEquals(pastBooking.getId(), firstBooking.getId());
    }

    @Test
    public void checkDates_PositiveTestCase() {
        BookingDtoRequest bookingDto = BookingDtoRequest.builder()
                .start(LocalDateTime.now().minusHours(2))
                .end(LocalDateTime.now().minusHours(1))
                .itemId(itemDtoFromDB.getId())
                .build();
        List<BookingDtoRequest> bookingDtos = List.of(bookingDto);
        BookingForResponse firstBooking = bookingService.addBooking(secondTestUser.getId(), bookingDto);
        BookingDtoRequest bookingDto1 = BookingDtoRequest.builder()
                .start(LocalDateTime.now().minusHours(1))
                .end(LocalDateTime.now())
                .itemId(itemDtoFromDB.getId())
                .build();

        assertThrows(ObjectBadRequestException.class,
                () -> bookingService.addBooking(secondTestUser.getId(), bookingDto));
    }

    @Test
    public void testToItemBookingForResponsePositiveTest() {
        Booking booking = Booking.builder()
                .id(1L)
                .booker(User.builder().id(2L).build())
                .item(Item.builder().id(1L).name("Hole").build())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        BookingForResponse itemBookingInfoDto = BookingMapper.toBookingForResponseMapper(booking);

        assertEquals(1L, itemBookingInfoDto.getId());
        assertEquals(2L, itemBookingInfoDto.getBooker().getId());
        assertEquals(1L, itemBookingInfoDto.getItem().getId());
        assertEquals("Hole", itemBookingInfoDto.getItem().getName());
        assertEquals(booking.getStart(), itemBookingInfoDto.getStart());
        assertEquals(booking.getEnd(), itemBookingInfoDto.getEnd());
    }

    @Test
    public void testToItemBookingForResponseNegativeTest() {
        Booking booking = Booking.builder()
                .id(1L)
                .booker(User.builder().id(2L).build())
                .item(Item.builder().id(1L).name("Hole").build())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        BookingForResponse itemBookingInfoDto = BookingMapper.toBookingForResponseMapper(booking);

        assertNotEquals(2L, itemBookingInfoDto.getId());
        assertNotEquals(1L, itemBookingInfoDto.getBooker().getId());
        assertNotEquals(2L, itemBookingInfoDto.getItem().getId());
        assertNotEquals("Hol", itemBookingInfoDto.getItem().getName());
        assertNotEquals(booking.getStart().plusDays(1), itemBookingInfoDto.getStart());
        assertNotEquals(booking.getEnd().minusHours(2), itemBookingInfoDto.getEnd());
    }
}
