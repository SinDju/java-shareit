package ru.practicum.shareit.item;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemDaoImpl implements ItemDao {
    private Map<Long, Item> items = new HashMap<>();
    private static long id = 1;

    @Override
    public Item addItem(Item item) {
        if (items.containsValue(item)) {
            throw new ObjectNotFoundException("Такой объект уже существует");
        }
        item.setId(id++);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateIteme(long itemId, ItemDto itemDto) {
        Item oldItem = getItem(itemId).get();
        if (itemDto.getName() != null) {
            oldItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            oldItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            oldItem.setAvailable(itemDto.getAvailable());
        }
        return oldItem;
    }

    @Override
    public Optional<Item> getItem(long itemId) {
        if (!items.containsKey(itemId)) {
            return Optional.empty();
        }
        return Optional.of(items.get(itemId));
    }

    @Override
    public List<Item> getAllItemsUser(long userId) {
        return items.values().stream().filter(item -> item.getOwner() == userId).collect(Collectors.toList());
    }

    @Override
    public List<Item> getSearchOfText(String text) {
        List<Item> itemSearchOfText = new ArrayList<>();
        List<Item> itemList = getAllItems();
        for (Item item : itemList) {
            if ((StringUtils.containsIgnoreCase(item.getName(), text) ||
                    StringUtils.containsIgnoreCase(item.getDescription(), text))
                    && item.getAvailable() == true) {
                itemSearchOfText.add(item);
            }
        }
        return itemSearchOfText;
    }

    private List<Item> getAllItems() {
        return items.values().stream().collect(Collectors.toList());
    }
}
