package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.mapper.BookingForItemDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ObjectBadRequestException;
import ru.practicum.shareit.exception.ObjectForbiddenException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemForBookingDtoMapper;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    public ItemDto addItem(long userId, Item item) {
        User user = checkUser(userId);
        item.setOwner(user);
        Item addItem = itemRepository.save(item);
        return ItemMapper.toItemDto(addItem);
    }

    private User checkUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь с ID " +
                        userId + " не зарегистрирован!"));
    }

    @Transactional
    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        Item oldItem = checkItem(itemId);
        long owner = oldItem.getOwner().getId();
        if (userId != owner) {
            throw new ObjectForbiddenException("У пользователя  нет доступа к вещи");
        }
        if (itemDto.getName() != null) {
            oldItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            oldItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            oldItem.setAvailable(itemDto.getAvailable());
        }
        Item updateItem = itemRepository.save(oldItem);
        return ItemMapper.toItemDto(updateItem);
    }

    @Transactional
    @Override
    public ItemForBookingDto getItemDto(Long ownerId, long itemId) { //ID хозяина вещи в бронировании.
        Item item = checkItem(itemId);
        return fillWithBookingInfo(List.of(item), ownerId).get(0);
    }

    private Item checkItem(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Вещь с ID " +
                        itemId + " не зарегистрирован!"));
    }

    @Transactional
    @Override
    public List<ItemForBookingDto> getAllItemsUser(long userId) {
        checkUser(userId);
        return fillWithBookingInfo(itemRepository.findAllByOwnerIdOrderById(userId), userId);
    }

    @Transactional
    @Override
    public List<ItemDto> getSearchOfText(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        List<Item> itemList = getSearch(text);
        return itemList.stream().map(ItemMapper::toItemDto).collect(toList());
    }

    @Transactional
    public List<Item> getSearch(String text) {
        List<Item> itemSearchOfText = new ArrayList<>();
        List<Item> itemList = itemRepository.findAll().stream().collect(Collectors.toList());
        ;
        for (Item item : itemList) {
            if ((StringUtils.containsIgnoreCase(item.getName(), text) ||
                    StringUtils.containsIgnoreCase(item.getDescription(), text))
                    && item.getAvailable() == true) {
                itemSearchOfText.add(item);
            }
        }
        return itemSearchOfText;
    }

    private List<ItemForBookingDto> fillWithBookingInfo(List<Item> items, Long userId) {
        Map<Item, List<Comment>> comments = commentRepository.findByItemIn(
                        items, Sort.by(DESC, "created"))
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));
        Map<Item, List<Booking>> bookings = bookingRepository.findByItemInAndStatus(
                        items, Status.APPROVED,
                        Sort.by(DESC, "start"))
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
        if (item.getOwner().getId() != userId) {
            return ItemForBookingDtoMapper.toItemForBookingMapper(item, null, null,
                    CommentMapper.commentDtoList(comments));
        }
        Booking lastBooking = bookings.stream()
                .filter(b -> !b.getStart().isAfter(now))
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .findFirst()
                .orElse(null);

        Booking nextBooking = bookings.stream()
                .filter(b -> b.getStart().isAfter(now))
                .reduce((a, b) -> a.getStart().isBefore(b.getStart()) ? a : b)
                .orElse(null);
        BookingForItemDto lastBookingDto = lastBooking != null ?
                BookingForItemDtoMapper.toItemBookingInfoDto(lastBooking) : null;
        BookingForItemDto nextBookingDto = nextBooking != null ?
                BookingForItemDtoMapper.toItemBookingInfoDto(nextBooking) : null;
        return ItemForBookingDtoMapper.toItemForBookingMapper(item, lastBookingDto, nextBookingDto,
                CommentMapper.commentDtoList(comments));
    }

    @Transactional
    @Override
    public CommentDto addComment(long itemId, long userId, Comment comment) {
        //Не забудьте добавить проверку, что пользователь,
        // который пишет комментарий, действительно брал вещь в аренду.
        Item item = checkItem(itemId);
        User user = checkUser(userId);
        List<Booking> bookings = bookingRepository.findAll().stream()
                .filter(booking -> booking.getItem().getId() == itemId && booking.getStatus() == Status.APPROVED
                        && (booking.getEnd().equals(LocalDateTime.now()) || booking.getEnd().isBefore(LocalDateTime.now())))
                .collect(toList());
        if (bookings.isEmpty()) {
            throw new ObjectBadRequestException("Неверные параметры");
        }
        for (Booking booking : bookings) {
            if (booking.getBooker().getId() == userId) {
                comment.setItem(item);
                comment.setAuthor(user);
                comment.setCreated(LocalDateTime.now());
                commentRepository.save(comment);
            }
        }
        return CommentMapper.toCommentDto(comment);
    }
}

