package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    public Booking addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Valid @RequestBody BookingDto bookingDto) {
        log.info("POST запрос на создание вещи");
        return service.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking updateBooking(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestParam Boolean approved) {
        log.info("POST запрос на создание вещи");
        return service.updateBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBooking(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("POST запрос на создание вещи");
        return service.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<Booking> getAllBookingByUser(@RequestParam(defaultValue = "All") String state,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("POST запрос на создание вещи");
        return service.getAllBookingByUser(state, userId);
    }

    @GetMapping("/owner")
    public List<Booking> getAllBookingByOwner(@RequestParam(defaultValue = "All") String state,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("POST запрос на создание вещи");
        return service.getAllBookingByOwner(state, userId);
    }
}
