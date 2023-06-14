package ru.practicum.shareit.item.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemSearchOfTextDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
}
