package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.*;
import java.util.Objects;

@Data
@NonNull
public class UserDto {
    @Positive
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
        UserDto userDto = (UserDto) o;
        return Objects.equals(name, userDto.name) && Objects.equals(email, userDto.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
