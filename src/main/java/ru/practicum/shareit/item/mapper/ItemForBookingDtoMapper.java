package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemForBookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Component
public class ItemForBookingDtoMapper {
    public static ItemForBookingDto toItemForBookingMapper(Item item, BookingForItemDto lastBooking, BookingForItemDto nextBooking, List<CommentDto> comments) {
        return new ItemForBookingDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                comments);
    }

}
