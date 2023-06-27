package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.*;
import java.util.Objects;

@Data
@NonNull
@AllArgsConstructor
@Builder
public class UserDtoRequest {
    private final Long id;
    @NotBlank(groups = {Create.class})
    @Pattern(regexp = "[a-zA-Zа-яА-Я\\s]*", groups = {Create.class, Update.class})
    private final String name;
    @NotEmpty(groups = {Create.class})
    @Email(groups = {Create.class, Update.class})
    private final String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDtoRequest userDtoRequest = (UserDtoRequest) o;
        return Objects.equals(name, userDtoRequest.name) && Objects.equals(email, userDtoRequest.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
