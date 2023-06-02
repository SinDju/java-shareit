package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Entity
@Table(name = "bookings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking implements Comparable<Booking>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    private LocalDateTime end;
    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item; // вещь, которую пользователь бронирует;
    @OneToOne
    @JoinColumn(name = "booker_id")
    private User booker; // пользователь, который осуществляет бронирование
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private Status status;

    @Override
    public int compareTo(Booking o) {
        return getStart().compareTo(o.start);
    }
}
