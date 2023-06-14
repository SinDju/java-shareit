package ru.practicum.shareit.booking.dto;

import lombok.*;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingForItemDto {
    private Long id;
    private Long bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
}
