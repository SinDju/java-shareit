package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponseDto addItemRequest(long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestResponseDto> getItemRequestsByUserId(long userId);
}
