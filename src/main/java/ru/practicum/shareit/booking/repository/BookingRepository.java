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
                    "where b.booker.id = ?1 " +
                    "and ?2 between b.start and b.end " +
                    "order by b.start DESC")
    List<Booking> findAllCurrentBookingsByBooker(
            Long userId,
            LocalDateTime now);

    @Query
            ("select b from Booking b " +
                    "where b.item.owner.id = ?1 " +
                    "and ?2 between b.start and b.end " +
                    "order by b.start DESC")
    List<Booking> findAllCurrentBookingsByOwner(
            Long userId,
            LocalDateTime now);
}
