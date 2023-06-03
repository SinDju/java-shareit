package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingForItemDto;

@Component
public class BookingForItemDtoMapper {
    public static BookingForItemDto toItemBookingInfoDto(Booking booking) {
        return new BookingForItemDto(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd());
    }
}
