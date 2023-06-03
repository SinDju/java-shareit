package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;

import java.util.List;

public interface BookingService {
    BookingForResponse addBooking(long userId, BookingDto bookingDto);

    BookingForResponse updateBooking(long bookingId, long userId, Boolean approved);

    BookingForResponse getBooking(long bookingId, long userId);

    List<BookingForResponse> getAllBookingByUser(String state, long userId);

    List<BookingForResponse> getAllBookingByOwner(String state, long userId);
}
