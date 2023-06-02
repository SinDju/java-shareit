package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class CommentMapper {
    public static ItemDto toCommentDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }
}
