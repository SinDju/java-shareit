package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@UtilityClass
public class ItemMapper {
    public ItemDtoRequest toItemDto(Item item) {
        return new ItemDtoRequest(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public Item toItem(ItemDtoRequest itemDtoRequest) {
        Item item = new Item();
        item.setName(itemDtoRequest.getName());
        item.setDescription(itemDtoRequest.getDescription());
        item.setAvailable(itemDtoRequest.getAvailable());
        return item;
    }

    public ItemDtoResponse toItemDtoResponse(Item item) {
        return new ItemDtoResponse(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public ItemWithBookingDto toItemWithBookingDto(Item item) {
        return ItemWithBookingDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }

    public ItemForBookingDto toItemForBookingMapper(Item item, BookingForItemDto lastBooking, BookingForItemDto nextBooking, List<CommentDtoRequest> comments) {
        return new ItemForBookingDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                comments);
    }

    public ItemSearchOfTextDto toItemSearchOfTextDto(Item item) {
        return new ItemSearchOfTextDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }
}
