package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByItemInAndStatus(
            List<Item> items,
            Status status,
            Sort created);

    @Query
            ("select b from Booking b " +
                    "where b.item.id = ?1")
    List<Booking> findAllBookingsByItem(
            Long itemId);

    @Query
            ("select b from Booking b " +
                    "where b.item.id = ?1 " +
                    "and b.status = ?2 " +
                    "and b.end = ?3 " +
                    "or b.end < ?3")
    List<Booking> checkValidateBookingsFromItemAndStatus(
            Long itemId, Status status, LocalDateTime end);

    @Query
            ("select b from Booking b " +
                    "where b.item.id = ?1 " +
                    "and ?2 between b.start and b.end")
    List<Booking> checkValidateBookings(
            Long itemId, LocalDateTime bookingDtoStartIsBeforeOrAfter);

    @Query
            ("select b from Booking b " +
                    "where b.booker.id = ?1 " +
                    "order by b.start DESC")
    List<Booking> findAllBookingsByBooker(
            Long userId);

    @Query
            ("select b from Booking b " +
                    "where b.booker.id = ?1 " +
                    "and ?2 between b.start and b.end " +
                    "order by b.start DESC")
    List<Booking> findAllCurrentBookingsByBooker(
            Long userId,
            LocalDateTime now);

    @Query
            ("select b from Booking b " +
                    "where b.booker.id = ?1 " +
                    "and ?2 > b.end " +
                    "and b.status = ?3 " +
                    "order by b.start DESC")
    List<Booking> findAllPastBookingsByBooker(
            Long userId,
            LocalDateTime now, Status status);

    @Query
            ("select b from Booking b " +
                    "where b.booker.id = ?1 " +
                    "and b.start > ?2 " +
                    "order by b.start DESC")
    List<Booking> findAllFutureBookingsByBooker(
            Long userId,
            LocalDateTime now);

    @Query
            ("select b from Booking b " +
                    "where b.booker.id = ?1 " +
                    "and b.status = ?2 " +
                    "order by b.start DESC")
    List<Booking> findAllWaitingBookingsByBooker(
            Long userId,
            Status status);

    @Query
            ("select b from Booking b " +
                    "where b.booker.id = ?1 " +
                    "and b.status = ?2 " +
                    "or b.status = ?3 " +
                    "order by b.start DESC")
    List<Booking> findAllRegectedBookingsByBooker(
            Long userId,
            Status status, Status st);

    @Query
            ("select b from Booking b " +
                    "where b.item.owner.id = ?1 " +
                    "order by b.start DESC")
    List<Booking> findAllBookingsByOwner(
            Long userId);

    @Query
            ("select b from Booking b " +
                    "where b.item.owner.id = ?1 " +
                    "and ?2 between b.start and b.end " +
                    "order by b.start DESC")
    List<Booking> findAllCurrentBookingsByOwner(
            Long userId,
            LocalDateTime now);

    @Query
            ("select b from Booking b " +
                    "where b.item.owner.id = ?1 " +
                    "and ?2 > b.end " +
                    "and b.status = ?3 " +
                    "order by b.start DESC")
    List<Booking> findAllPastBookingsByOwner(
            Long userId,
            LocalDateTime now, Status status);

    @Query
            ("select b from Booking b " +
                    "where b.item.owner.id = ?1 " +
                    "and b.start > ?2 " +
                    "order by b.start DESC")
    List<Booking> findAllFutureBookingsByOwner(
            Long userId,
            LocalDateTime now);

    @Query
            ("select b from Booking b " +
                    "where b.item.owner.id = ?1 " +
                    "and b.status = ?2 " +
                    "order by b.start DESC")
    List<Booking> findAllWaitingBookingsByOwner(
            Long userId,
            Status status);

    @Query
            ("select b from Booking b " +
                    "where b.item.owner.id = ?1 " +
                    "and b.status = ?2 " +
                    "or b.status = ?3 " +
                    "order by b.start DESC")
    List<Booking> findAllRegectedBookingsByOwner(
            Long userId,
            Status status, Status st);
}
