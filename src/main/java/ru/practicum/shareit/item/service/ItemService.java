package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto addItem(long userId, Item item);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    ItemForBookingDto getItemDto(Long ownerId, long itemId);

    List<ItemForBookingDto> getAllItemsUser(long userId);

    List<ItemDto> getSearchOfText(String text);

    CommentDto addComment(long itemId, long userId, Comment comment);
}
