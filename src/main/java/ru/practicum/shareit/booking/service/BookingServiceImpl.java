package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StateBooking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ObjectBadRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    @Override
    public BookingForResponse addBooking(long userId, BookingDtoRequest bookingDtoRequest) {
        Item item = itemRepository.findById(bookingDtoRequest.getItemId()).orElseThrow(() ->
                new ObjectNotFoundException("Вещь с ID " +
                        bookingDtoRequest.getItemId() + " не зарегистрирована!"));
        if (!item.getAvailable()) {
            throw new ObjectBadRequestException("Вещь не доступна для бронирования");
        }
        User user = checkUser(userId);
        validateBooking(bookingDtoRequest, item, user);
        Booking booking = BookingMapper.toBooking(bookingDtoRequest, item, user);
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setBooker(user);
        Booking result = bookingRepository.save(booking);
        return BookingMapper.toBookingForResponseMapper(result);
    }

    @Transactional
    @Override
    public BookingForResponse updateBooking(long bookingId, long userId, Boolean approved) {
        checkUser(userId);
        Booking booking = checkBooking(bookingId);
        Item item = booking.getItem();
        if (item.getOwner().getId() != userId) {
            throw new ObjectNotFoundException("Пользователь не является владельцем вещи " +
                    "и не может подтвердить бронирование");
        }
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new ObjectBadRequestException("Данное бронирование уже было обработано и имеет статус "
                    + booking.getStatus());
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.toBookingForResponseMapper(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public BookingForResponse getBooking(long bookingId, long userId) {
        checkUser(userId);
        checkBooking(bookingId);
        Booking booking = bookingRepository.findById(bookingId).filter(booking1 ->
                booking1.getBooker().getId() == userId
                        || booking1.getItem().getOwner().getId() == userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь не является владельцем вещи " +
                        "и не может подтвердить бронирование"));
        ;
        return BookingMapper.toBookingForResponseMapper(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingForResponse> getAllBookingByUser(String state, long userId, int from, int size) {
        checkUser(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> result = new ArrayList<>();
        StateBooking stateBooking = StateBooking.getStateFromText(state);
        Pageable pageable = PageRequest.of(from / size, size);

        switch (stateBooking) {
            case ALL:
                result = bookingRepository.findAllBookingsByBooker(userId, pageable).getContent();
                break;
            case CURRENT:
                result = bookingRepository.findAllCurrentBookingsByBooker(userId, now, pageable).getContent();
                break;
            case PAST:
                result = bookingRepository.findAllPastBookingsByBooker(userId, now, Status.APPROVED, pageable)
                        .getContent();
                break;
            case FUTURE:
                result = bookingRepository.findAllFutureBookingsByBooker(userId, now, pageable).getContent();
                break;
            case WAITING:
                result = bookingRepository.findAllWaitingBookingsByBooker(userId, Status.WAITING, pageable)
                        .getContent();
                break;
            case REJECTED:
                result = bookingRepository.findAllRegectedBookingsByBooker(userId, Status.REJECTED, Status.CANCELED,
                        pageable).getContent();
                break;
        }

        return result.stream().map(BookingMapper::toBookingForResponseMapper)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingForResponse> getAllBookingByOwner(String state, long userId, int from, int size) {
        checkUser(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> result = new ArrayList<>();
        StateBooking stateBooking = StateBooking.getStateFromText(state);
        Pageable pageable = PageRequest.of(from / size, size);

        switch (stateBooking) {
            case ALL:
                result = bookingRepository.findAllBookingsByOwner(userId, pageable).getContent();
                break;
            case CURRENT:
                result = bookingRepository.findAllCurrentBookingsByOwner(userId, now, pageable).getContent();
                break;
            case PAST:
                result = bookingRepository.findAllPastBookingsByOwner(userId, now, Status.APPROVED, pageable)
                        .getContent();
                break;
            case FUTURE:
                result = bookingRepository.findAllFutureBookingsByOwner(userId, now, pageable).getContent();
                break;
            case WAITING:
                result = bookingRepository.findAllWaitingBookingsByOwner(userId, Status.WAITING, pageable)
                        .getContent();
                break;
            case REJECTED:
                result = bookingRepository.findAllRegectedBookingsByOwner(userId, Status.REJECTED,
                        Status.CANCELED, pageable).getContent();
                break;
        }

        return result.stream().map(BookingMapper::toBookingForResponseMapper)
                .collect(Collectors.toList());
    }

    private User checkUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь с ID " +
                        userId + " не зарегистрирован!"));
    }

    private Booking checkBooking(long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new ObjectNotFoundException("Бронь с ID " +
                        bookingId + " не зарегистрирован!"));
    }

    public void validateBooking(BookingDtoRequest bookingDtoRequest, Item item, User booker) {
        if (item.getOwner().equals(booker)) {
            throw new ObjectNotFoundException("Создать бронь на свою вещь нельзя.");
        }
        List<Booking> bookings = bookingRepository.checkValidateBookings(item.getId(), bookingDtoRequest.getStart());
        if (bookings != null && !bookings.isEmpty()) {
            throw new ObjectBadRequestException("Найдено пересечение броней на эту вещь с name = "
                    + item.getName() + ".");
        }
    }
}
