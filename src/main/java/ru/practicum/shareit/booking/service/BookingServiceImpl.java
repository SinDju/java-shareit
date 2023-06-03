package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.mapper.BookingForResponseMapper;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ObjectBadRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
    public BookingForResponse addBooking(long userId, BookingDto bookingDto) {
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new ObjectNotFoundException("Вещь с ID " +
                        bookingDto.getItemId() + " не зарегистрирован!"));
        if (!item.getAvailable()) {
            throw new ObjectBadRequestException("Вещь не доступна для бронирования");
        }
        User user = checkUser(userId);
        validateBooking(bookingDto, item, user);
        Booking booking = BookingMapper.toBooking(bookingDto, item, user);
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setBooker(user);
        Booking result = bookingRepository.save(booking);
        return BookingForResponseMapper.toBookingForResponseMapper(result);
    }

    private void validateBooking(BookingDto bookingDto, Item item, User booker) {
        if (item.getOwner().equals(booker)) {
            throw new ObjectNotFoundException("Создать бронь на свою вещь нельзя.");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ObjectBadRequestException("Начало бронирования не может быть в прошлом"
                    + bookingDto.getStart() + ".");
        }
        if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new ObjectBadRequestException("Окончание бронирования не может быть в прошлом.");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ObjectBadRequestException("Окончание бронирования не может быть раньше его начала.");
        }
        if (bookingDto.getEnd().equals(bookingDto.getStart())) {
            throw new ObjectBadRequestException("Начало и окончание бронирования " +
                    "не может быть в одно и тоже время.");
        }
        List<Booking> bookings = item.getBookings();
        if (bookings != null && !bookings.isEmpty()) {
            for (Booking booking : bookings) {
                if (!(booking.getEnd().isBefore(bookingDto.getStart()) ||
                        booking.getStart().isAfter(bookingDto.getStart()))) {
                    throw new ObjectBadRequestException("Найдено пересечение броней на эту вещь с name = "
                            + item.getName() + ".");
                }
            }
        }
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
        Booking result = bookingRepository.save(booking);
        return BookingForResponseMapper.toBookingForResponseMapper(result);
    }


    @Transactional
    @Override
    public BookingForResponse getBooking(long bookingId, long userId) {
        User authorBroking = checkUser(userId);
        checkBooking(bookingId);
        Booking booking = bookingRepository.findById(bookingId).filter(booking1 ->
                booking1.getBooker().getId() == userId
                        || booking1.getItem().getOwner().getId() == userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь не является владельцем вещи " +
                        "и не может подтвердить бронирование"));
        ;
        return BookingForResponseMapper.toBookingForResponseMapper(booking);
    }

    @Transactional
    @Override
    public List<BookingForResponse> getAllBookingByUser(String state, long userId) {
        checkUser(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> result = new ArrayList<>();
        List<Booking> listAllBookingByUser = bookingRepository.findAll().stream()
                .filter(booking -> booking.getBooker().getId() == userId).collect(Collectors.toList());

        switch (state) {
            case ("ALL"):
                result = listAllBookingByUser.stream()
                        .sorted(Comparator.comparing(Booking::getEnd).reversed())
                        .collect(Collectors.toList());
                break;
            case ("CURRENT"):
                result = bookingRepository.findAllCurrentBookingsByBooker(userId, now);
                break;
            case ("PAST"):
                result = listAllBookingByUser.stream()
                        .filter(booking -> now.isAfter(booking.getEnd())
                                && booking.getStatus() == Status.APPROVED)
                        .sorted(Comparator.comparing(Booking::getEnd).reversed())
                        .collect(Collectors.toList());
                break;
            case ("FUTURE"):
                result = listAllBookingByUser.stream()
                        .filter(booking -> booking.getStart().isAfter(now))
                        .sorted(Comparator.comparing(Booking::getEnd).reversed())
                        .collect(Collectors.toList());
                break;
            case ("WAITING"):
                result = listAllBookingByUser.stream()
                        .filter(booking -> booking.getStatus() == Status.WAITING)
                        .sorted(Comparator.comparing(Booking::getEnd).reversed())
                        .collect(Collectors.toList());
                break;
            case ("REJECTED"):
                result = listAllBookingByUser.stream()
                        .filter(booking -> booking.getStatus() == Status.REJECTED ||
                                booking.getStatus() == Status.CANCELED)
                        .sorted(Comparator.comparing(Booking::getEnd).reversed())
                        .collect(Collectors.toList());
                break;
            case ("UNSUPPORTED_STATUS"):
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }

        return result.stream().map(BookingForResponseMapper::toBookingForResponseMapper)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<BookingForResponse> getAllBookingByOwner(String state, long userId) {
        checkUser(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> result = new ArrayList<>();
        List<Booking> listAllBookingByUser = bookingRepository.findAll().stream()
                .filter(booking -> booking.getItem().getOwner().getId() == userId).collect(Collectors.toList());

        switch (state) {
            case ("ALL"):
                result = listAllBookingByUser.stream()
                        .sorted(Comparator.comparing(Booking::getEnd).reversed())
                        .collect(Collectors.toList());
                break;
            case ("CURRENT"):
                result = bookingRepository.findAllCurrentBookingsByOwner(userId, now);
                break;
            case ("PAST"):
                result = listAllBookingByUser.stream()
                        .filter(booking -> now.isAfter(booking.getEnd())
                                && booking.getStatus() == Status.APPROVED)
                        .sorted(Comparator.comparing(Booking::getEnd).reversed())
                        .collect(Collectors.toList());
                break;
            case ("FUTURE"):
                result = listAllBookingByUser.stream()
                        .filter(booking -> booking.getStart().isAfter(now))
                        .sorted(Comparator.comparing(Booking::getEnd).reversed())
                        .collect(Collectors.toList());
                break;
            case ("WAITING"):
                result = listAllBookingByUser.stream()
                        .filter(booking -> booking.getStatus() == Status.WAITING)
                        .sorted(Comparator.comparing(Booking::getEnd).reversed())
                        .collect(Collectors.toList());
                break;
            case ("REJECTED"):
                result = listAllBookingByUser.stream()
                        .filter(booking -> booking.getStatus() == Status.REJECTED ||
                                booking.getStatus() == Status.CANCELED)
                        .sorted(Comparator.comparing(Booking::getEnd).reversed())
                        .collect(Collectors.toList());
                break;
            case ("UNSUPPORTED_STATUS"):
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }

        return result.stream().map(BookingForResponseMapper::toBookingForResponseMapper)
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
}
