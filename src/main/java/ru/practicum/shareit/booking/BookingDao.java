package ru.practicum.shareit.booking;


import java.util.List;

public interface BookingDao {
    Booking addBooking(Booking booking);

    Booking updateBooking(long bookingId, long userId, Boolean approved);

    Booking getBooking(long bookingId, long userId);

    List<Booking> getAllBookingByUser(String state, long userId);

    List<Booking> getAllBookingByOwner(String state, long userId);
}
