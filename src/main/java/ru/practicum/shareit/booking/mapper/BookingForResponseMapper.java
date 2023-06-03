package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserWithIdDto;

public class BookingForResponseMapper {
    public static BookingForResponse toBookingForResponseMapper(Booking booking) {
        return BookingForResponse.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(new ItemWithBookingDto(booking.getItem().getId(), booking.getItem().getName()))
                .booker(new UserWithIdDto(booking.getBooker().getId()))
                .build();
    }

    public static Booking toBooking(BookingForResponse bookingForResponse, Item item, User user) {
        Booking booking = new Booking();
        booking.setId(bookingForResponse.getId());
        booking.setStart(bookingForResponse.getStart());
        booking.setEnd(bookingForResponse.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(bookingForResponse.getStatus());
        return booking;
    }
}
