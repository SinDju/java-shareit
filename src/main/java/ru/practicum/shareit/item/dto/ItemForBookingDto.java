package ru.practicum.shareit.item.dto;


import lombok.*;
import ru.practicum.shareit.booking.dto.BookingForItemDto;

import java.util.List;

@Data
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ItemForBookingDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;
    private final List<CommentDtoRequest> comments;
}
