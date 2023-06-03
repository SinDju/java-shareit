package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * TODO Sprint add-controllers.
 */
@Entity
@Table(name = "items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    @Column(name = "is_available")
    private Boolean available;
    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner; // владелец вещи
    @OneToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request; // ссылка на запрос, по которому создана вещь
    @OneToMany(mappedBy = "item")
    private List<Booking> bookings;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name) && Objects.equals(description, item.description)
                && Objects.equals(available, item.available) && Objects.equals(owner, item.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, available, owner);
    }
}
