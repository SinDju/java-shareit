package ru.practicum.shareit.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    private Long id;
    @NotBlank
    private String description;
    @NotNull
    private Long requestor; // пользователь создавший запрос
    private LocalDateTime created; //дата и время создания запроса
}
