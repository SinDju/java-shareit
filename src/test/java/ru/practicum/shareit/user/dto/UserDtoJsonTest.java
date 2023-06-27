package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class UserDtoJsonTest {
    @Autowired
    private JacksonTester<UserDtoResponse> json;
    @Autowired
    private JacksonTester<UserDtoRequest> jsonUserDtoRequest;

    @Test
    void testItemDtoRequest() throws IOException {
        UserDtoRequest userDtoRequest = UserDtoRequest.builder()
                .id(1L)
                .name("Sukiyaki")
                .email("sukiyaki@mail.ru")
                .build();
        JsonContent<UserDtoRequest> result = jsonUserDtoRequest.write(userDtoRequest);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("Sukiyaki");
        assertThat(result).extractingJsonPathStringValue("$.email")
                .isEqualTo("sukiyaki@mail.ru");
    }

    @Test
    void testItemDtoResponse() throws IOException {
        UserDtoResponse userDtoResponse = UserDtoResponse.builder()
                .id(1L)
                .name("Sukiyaki")
                .email("sukiyaki@mail.ru")
                .build();
        JsonContent<UserDtoResponse> result = json.write(userDtoResponse);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("Sukiyaki");
        assertThat(result).extractingJsonPathStringValue("$.email")
                .isEqualTo("sukiyaki@mail.ru");
    }
}
