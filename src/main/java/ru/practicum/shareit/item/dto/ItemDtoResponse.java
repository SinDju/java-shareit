package ru.practicum.shareit.item.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ItemDtoResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
}
