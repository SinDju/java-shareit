package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemForResponseDto;
import ru.practicum.shareit.user.UserWithIdDto;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class BookingForResponse {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ItemForResponseDto item;
    private UserWithIdDto booker;
    private Status status;

}
