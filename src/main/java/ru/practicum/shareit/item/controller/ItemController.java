package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForBookingDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.Create;

import javax.validation.Valid;
import java.util.List;

@Validated
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @Validated({Create.class})
    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("POST запрос на создание вещи");
        return service.addItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId,
                                 @Valid @RequestBody CommentDto commentDto) {
        log.info("POST запрос на создание вещи");
        return service.addComment(itemId, userId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("PATCH запрос на обновление вещи");
        return service.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemForBookingDto getItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                     @PathVariable Long itemId) {
        log.info("GET запрос на получение вещи с ID: {}", itemId);
        return service.getItemDto(ownerId, itemId);
    }

    @GetMapping
    public List<ItemForBookingDto> getAllItemsUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GET запрос на получение всех вещей пользователя с ID: {}", userId);
        return service.getAllItemsUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getSearchOfText(@RequestParam String text) {
        log.info("GET запрос на получение всех вещей с текстом: {}", text);
        return service.getSearchOfText(text);
    }
}
