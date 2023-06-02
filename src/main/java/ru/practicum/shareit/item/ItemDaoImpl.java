package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemDaoImpl implements ItemDao {
    private final ItemRepository repository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Item addItem(Item item) {
        /*if (repository.existsById(item.getId())) {
            throw new ObjectNotFoundException("Такой объект уже существует");
        }*/
        return repository.save(item);
    }

    @Transactional
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
        return repository.save(oldItem);
    }

    @Transactional
    @Override
    public Optional<Item> getItem(long itemId) {
        if (!repository.existsById(itemId)) {
            return Optional.empty();
        }
        return Optional.of(repository.findById(itemId).get());
    }

    @Override
    public List<Item> getAllItemsUser(long userId) {
        return repository.findAll().stream().filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Transactional
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

    @Override
    public CommentDto addComment(long itemId, long userId, Comment comment) {
        return null;
    }

    private List<Item> getAllItems() {
        return repository.findAll().stream().collect(Collectors.toList());
    }
}
