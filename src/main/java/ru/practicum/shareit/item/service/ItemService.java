package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.dto.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

public interface ItemService {
    ItemDtoResponse addItem(long userId, ItemDtoRequest itemDtoRequest);

    ItemDtoResponse updateItem(long userId, long itemId, ItemDtoRequest itemDtoRequest);

    ItemForBookingDto getItemDto(Long ownerId, long itemId);

    List<ItemForBookingDto> getAllItemsUser(long userId, int from, int size);

    List<ItemSearchOfTextDto> getSearchOfText(String text, int from, int size);

    CommentDtoResponse addComment(long itemId, long userId, CommentDtoRequest commentDtoRequest);
}
