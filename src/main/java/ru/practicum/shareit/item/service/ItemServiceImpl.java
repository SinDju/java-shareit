package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ObjectBadRequestException;
import ru.practicum.shareit.exception.ObjectForbiddenException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public ItemDtoResponse addItem(long userId, ItemDtoRequest itemDtoRequest) {
        User user = checkUser(userId);
        Item item = ItemMapper.toItem(itemDtoRequest);
        item.setOwner(user);
        Item addItem = itemRepository.save(item);
        return ItemMapper.toItemDtoResponse(addItem);
    }

    @Transactional
    @Override
    public ItemDtoResponse updateItem(long userId, long itemId, ItemDtoRequest itemDtoRequest) {
        Item oldItem = checkItem(itemId);
        long owner = oldItem.getOwner().getId();
        if (userId != owner) {
            throw new ObjectForbiddenException("У пользователя  нет доступа к вещи");
        }
        if (itemDtoRequest.getName() != null && !itemDtoRequest.getName().isBlank()) {
            oldItem.setName(itemDtoRequest.getName());
        }
        if (itemDtoRequest.getDescription() != null && !itemDtoRequest.getDescription().isBlank()) {
            oldItem.setDescription(itemDtoRequest.getDescription());
        }
        if (itemDtoRequest.getAvailable() != null) {
            oldItem.setAvailable(itemDtoRequest.getAvailable());
        }
        return ItemMapper.toItemDtoResponse(oldItem);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemForBookingDto getItemDto(Long ownerId, long itemId) { //ID хозяина вещи в бронировании.
        Item item = checkItem(itemId);
        return fillWithBookingInfo(List.of(item), ownerId).get(0);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemForBookingDto> getAllItemsUser(long userId) {
        checkUser(userId);
        return fillWithBookingInfo(itemRepository.findAllByOwnerIdOrderById(userId), userId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemSearchOfTextDto> getSearchOfText(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        List<Item> itemList = getSearch(text);
        return itemList.stream().map(ItemMapper::toItemSearchOfTextDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<Item> getSearch(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findByNameOrDescription(text);
    }

    @Transactional
    @Override
    public CommentDtoResponse addComment(long itemId, long userId, CommentDtoRequest commentDtoRequest) {
        Item item = checkItem(itemId);
        User user = checkUser(userId);
        Boolean checkValidate = bookingRepository.checkValidateBookingsFromItemAndStatus(itemId, userId,
                Status.APPROVED, LocalDateTime.now());
        if (!checkValidate) {
            throw new ObjectBadRequestException("Неверные параметры");
        }
        commentDtoRequest.setCreated(LocalDateTime.now());
        Comment comment = CommentMapper.toComment(commentDtoRequest, item, user);
        return CommentMapper.toCommentDtoResponse(commentRepository.save(comment));
    }

    private Item checkItem(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Вещь с ID " +
                        itemId + " не зарегистрирован!"));
    }

    private User checkUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь с ID " +
                        userId + " не зарегистрирован!"));
    }

    private List<ItemForBookingDto> fillWithBookingInfo(List<Item> items, Long userId) {
        Map<Item, List<Comment>> comments = commentRepository.findByItemIn(
                        items, Sort.by(DESC, "created"))
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));
        Map<Item, List<Booking>> bookings = bookingRepository.findByItemInAndStatus(
                        items,  Status.APPROVED, Sort.by(DESC, "start"))
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));
        LocalDateTime now = LocalDateTime.now();
        return items.stream().map(item -> addBookingAndComment(item, userId, comments.getOrDefault(item, List.of()),
                        bookings.getOrDefault(item, List.of()), now))
                .collect(toList());
    }

    private ItemForBookingDto addBookingAndComment(Item item,
                                                   Long userId,
                                                   List<Comment> comments,
                                                   List<Booking> bookings,
                                                   LocalDateTime now) {
        if (item.getOwner().getId().longValue() != userId.longValue()) {
            return ItemMapper.toItemForBookingMapper(item, null, null,
                    CommentMapper.commentDtoList(comments));
        }
        Booking lastBooking = bookings.stream()
                .filter(b -> !b.getStart().isAfter(now))
                .findFirst()
                .orElse(null);

        Booking nextBooking = bookings.stream()
                .filter(b -> b.getStart().isAfter(now))
                .reduce((a, b) -> a.getStart().isBefore(b.getStart()) ? a : b)
                .orElse(null);
        BookingForItemDto lastBookingDto = lastBooking != null ?
                BookingMapper.toItemBookingInfoDto(lastBooking) : null;
        BookingForItemDto nextBookingDto = nextBooking != null ?
                BookingMapper.toItemBookingInfoDto(nextBooking) : null;
        return ItemMapper.toItemForBookingMapper(item, lastBookingDto, nextBookingDto,
                CommentMapper.commentDtoList(comments));
    }
}

