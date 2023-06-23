package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDtoRequest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class UserDtoTest {
    @Autowired
    private JacksonTester<UserDtoRequest> json;

    @Test
    void testUserDto() throws Exception {
        UserDtoRequest userDto = new UserDtoRequest(
                1L,
                "John",
                "john.doe@mail.com");

        JsonContent<UserDtoRequest> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john.doe@mail.com");
    }
}
