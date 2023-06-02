package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.ObjectBadRequestException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemDao;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDao;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingDao bookingDao;
    private final UserDao userDao;
    private final ItemDao itemDao;

    @Transactional
    @Override
    public Booking addBooking(long userId, BookingDto bookingDto) {
        Item item = itemDao.getItem(bookingDto.getItemId()).orElseThrow(() ->
                new ObjectNotFoundException("Вещь с ID " +
                        bookingDto.getItemId() + " не зарегистрирован!"));
        if (!item.getAvailable()) {
            throw new ObjectBadRequestException("Вещь не доступна для бронирования");
        }
        User user = userDao.getUser(userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь с ID " +
                        userId + " не зарегистрирован!"));
        validateBooking(bookingDto, item, user);
        Booking booking = BookingMapper.toBooking(bookingDto, item, user);
        booking.setStatus(Status.WAITING);
        booking.setItem(item);
        booking.setBooker(user);
        return bookingDao.addBooking(booking);
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
    public Booking updateBooking(long bookingId, long userId, Boolean approved) {
        return bookingDao.updateBooking(bookingId, userId, approved);
    }

    @Transactional
    @Override
    public Booking getBooking(long bookingId, long userId) {
        return bookingDao.getBooking(bookingId, userId);
    }

    @Transactional
    @Override
    public List<Booking> getAllBookingByUser(String state, long userId) {
        return bookingDao.getAllBookingByUser(state, userId);
    }

    @Transactional
    @Override
    public List<Booking> getAllBookingByOwner(String state, long userId) {
        return bookingDao.getAllBookingByOwner(state, userId);
    }
}
