package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
    Item addItem(Item item);

    Item updateIteme(long itemId, ItemDto itemDto);

    Optional<Item> getItem(long itemId);

    List<Item> getAllItemsUser(long userId);

    List<Item> getSearchOfText(String text);
}
