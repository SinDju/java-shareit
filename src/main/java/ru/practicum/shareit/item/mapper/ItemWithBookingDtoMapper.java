package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;


public class ItemWithBookingDtoMapper {
    public static ItemWithBookingDto toItemWithBookingDto(Item item) {
        return ItemWithBookingDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }
}
