package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    Booking addBooking(long userId, BookingDto bookingDto);

    Booking updateBooking(long bookingId, long userId, Boolean approved);

    Booking getBooking(long bookingId, long userId);

    List<Booking> getAllBookingByUser(String state, long userId);

    List<Booking> getAllBookingByOwner(String state, long userId);
}
