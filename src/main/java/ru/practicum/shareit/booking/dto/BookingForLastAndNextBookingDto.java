package ru.practicum.shareit.booking.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class BookingForLastAndNextBookingDto {
    private Long id;
    private Long bookerId;
}
