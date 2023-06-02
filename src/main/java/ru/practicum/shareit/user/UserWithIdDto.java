package ru.practicum.shareit.user;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserWithIdDto {
    private Long id;            //ID пользователя.
}
