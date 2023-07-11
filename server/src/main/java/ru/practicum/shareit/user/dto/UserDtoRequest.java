package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Objects;

@Data
@NonNull
@AllArgsConstructor
@Builder
public class UserDtoRequest {
    private final Long id;
    private final String name;
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
