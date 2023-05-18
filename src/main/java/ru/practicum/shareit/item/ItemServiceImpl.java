package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectForbiddenException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemDao itemDao;

    @Override
    public ItemDto addItem(long userId, Item item) {
        userService.getUser(userId);
        item.setOwner(userId);
        Item addItem = itemDao.addItem(item);
        return ItemMapper.toItemDto(addItem);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        Item oldItem = getItem(itemDao.getItem(itemId).get().getId());
        long owner = oldItem.getOwner();
        if (userId != owner) {
            throw new ObjectForbiddenException("У пользователя с ID {} нет доступа к вещи");
        }
        Item updateItem = itemDao.updateIteme(itemId, itemDto);
        return ItemMapper.toItemDto(updateItem);
    }

    @Override
    public ItemDto getItemDto(long itemId) {
        return ItemMapper.toItemDto(getItem(itemId));
    }

    private Item getItem(long itemId) {
        return itemDao.getItem(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Вещь с ID " +
                        itemId + " не зарегистрирован!"));
    }

    @Override
    public List<ItemDto> getAllItemsUser(long userId) {
        return itemDao.getAllItemsUser(userId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getSearchOfText(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        List<Item> itemList = itemDao.getSearchOfText(text);
        return itemList.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
