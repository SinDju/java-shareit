package ru.practicum.shareit.booking.mapper;


import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserWithIdDto;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class BookingMapper {
    public BookingDtoRequest toBookingDto(Booking booking) {
        return new BookingDtoRequest(booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId()
        );
    }

    public static Booking toBooking(BookingDtoRequest bookingDtoRequest, Item item, User user) {
        Booking booking = new Booking();
        booking.setStart(bookingDtoRequest.getStart());
        booking.setEnd(bookingDtoRequest.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        return booking;
    }

    public BookingForItemDto toItemBookingInfoDto(Booking booking) {
        return new BookingForItemDto(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd());
    }

    public BookingForResponse toBookingForResponseMapper(Booking booking) {
        return BookingForResponse.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(new ItemWithBookingDto(booking.getItem().getId(), booking.getItem().getName()))
                .booker(new UserWithIdDto(booking.getBooker().getId()))
                .build();
    }

    public Booking toBooking(BookingForResponse bookingForResponse, Item item, User user) {
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
