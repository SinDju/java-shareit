package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ItemDtoResponse addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @RequestBody ItemDtoRequest itemDtoRequest) {
        log.info("POST запрос на создание вещи");
        return service.addItem(userId, itemDtoRequest);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoResponse addComment(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody CommentDtoRequest commentDtoRequest) {
        log.info("POST запрос на создание вещи");
        return service.addComment(itemId, userId, commentDtoRequest);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoResponse updateItem(@RequestBody ItemDtoRequest itemDtoRequest,
                                      @RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.info("PATCH запрос на обновление вещи");
        return service.updateItem(userId, itemId, itemDtoRequest);
    }

    @GetMapping("/{itemId}")
    public ItemForBookingDto getItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                     @PathVariable Long itemId) {
        log.info("GET запрос на получение вещи с ID: {}", itemId);
        return service.getItemDto(ownerId, itemId);
    }

    @GetMapping
    public List<ItemForBookingDto> getAllItemsUser(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        log.info("GET запрос на получение всех вещей пользователя с ID: {}", userId);
        return service.getAllItemsUser(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemSearchOfTextDto> getSearchOfText(
            @RequestParam String text,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        log.info("GET запрос на получение всех вещей с текстом: {}", text);
        return service.getSearchOfText(text, from, size);
    }
}
