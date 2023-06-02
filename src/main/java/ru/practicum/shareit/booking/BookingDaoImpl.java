package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectBadRequestException;
import ru.practicum.shareit.exception.ObjectForbiddenException;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookingDaoImpl implements BookingDao {
    private final BookingRepository repository;

    @Override
    public Booking addBooking(Booking booking) {
        return repository.save(booking);
    }

    @Override
    public Booking updateBooking(long bookingId, long userId, Boolean approved) {
        Booking booking = getBooking(bookingId);
        Item item = booking.getItem();
        if (item.getOwner().getId() != userId) {
            throw new ObjectForbiddenException("Пользователь не является владельцем вещи " +
                    "и не может подтвердить бронирование");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
            repository.save(booking);
        }
        booking.setStatus(Status.REJECTED);
        repository.save(booking);
        return booking;
    }

    private Booking getBooking(long bookingId) {
        return repository.findById(bookingId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь с ID " +
                        bookingId + " не зарегистрирован!"));
    }

    @Override
    public Booking getBooking(long bookingId, long userId) {
        Booking booking = getBooking(bookingId);
        Item item = booking.getItem();
        if (item.getOwner().getId() != userId || booking.getBooker().getId() != userId) {
            throw new ObjectForbiddenException("Пользователь не является владельцем вещи " +
                    "и не может подтвердить бронирование");
        }
        return getBooking(bookingId);
    }

    @Override
    public List<Booking> getAllBookingByUser(String state, long userId) {
        List<Booking> listAllBookingByUser = repository.findAll().stream()
                .filter(booking -> booking.getBooker().getId() == userId).collect(Collectors.toList());
        return getAllBookingByUserOrOwner(state, listAllBookingByUser);
    }

    @Override
    public List<Booking> getAllBookingByOwner(String state, long userId) {
        List<Booking> listAllBookingByOwner = repository.findAll().stream()
                .filter(booking -> booking.getItem().getOwner().getId() == userId).collect(Collectors.toList());
        return getAllBookingByUserOrOwner(state, listAllBookingByOwner);
    }

    private List<Booking> getAllBookingByUserOrOwner(String state, List<Booking> listAllBookingByUserOrOwner) {
        final LocalDateTime nowDateTime = LocalDateTime.now();
        switch (state) {
            case ("All"):
                return listAllBookingByUserOrOwner.stream().sorted()
                        .collect(Collectors.toList());
            case ("CURRENT"):
                return listAllBookingByUserOrOwner.stream()
                        .filter(booking -> booking.getStatus() == Status.APPROVED).sorted()
                        .collect(Collectors.toList());
            case ("PAST"):
                return listAllBookingByUserOrOwner.stream()
                        .filter(booking -> booking.getEnd().equals(nowDateTime))
                        .collect(Collectors.toList());
            case ("FUTURE"):
                return listAllBookingByUserOrOwner.stream()
                        .filter(booking -> booking.getStart().isAfter(nowDateTime)).sorted()
                        .collect(Collectors.toList());
            case ("WAITING"):
                return listAllBookingByUserOrOwner.stream()
                        .filter(booking -> booking.getStatus() == Status.WAITING).sorted()
                        .collect(Collectors.toList());
            case ("REJECTED"):
                return listAllBookingByUserOrOwner.stream()
                        .filter(booking -> booking.getStatus() == Status.REJECTED ||
                                booking.getStatus() == Status.CANCELED).sorted()
                        .collect(Collectors.toList());
            default:
                throw new ObjectBadRequestException("Такого параметра state не существует");
        }
    }


}
