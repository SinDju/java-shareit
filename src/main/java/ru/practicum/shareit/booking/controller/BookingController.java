package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingForResponse addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @Valid @RequestBody BookingDto bookingDto) {
        log.info("POST запрос на создание брони");
        return service.addBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingForResponse updateBooking(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam Boolean approved) {
        log.info("Patch запрос на обнавление брони");
        return service.updateBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingForResponse getBooking(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get запрос на получение брони");
        return service.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingForResponse> getAllBookingByUser(@RequestParam(defaultValue = "ALL") String state,
                                                        @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get запрос на получение листа бронирования user с Id {} со статусом {}", userId, state);
        return service.getAllBookingByUser(state, userId);
    }

    @GetMapping("/owner")
    public List<BookingForResponse> getAllBookingByOwner(@RequestParam(defaultValue = "ALL") String state,
                                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get запрос на получение бронирования броней owner с Id {} со статусом {}", userId, state);
        return service.getAllBookingByOwner(state, userId);
    }
}
