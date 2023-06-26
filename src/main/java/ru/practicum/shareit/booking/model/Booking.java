package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {
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
}
