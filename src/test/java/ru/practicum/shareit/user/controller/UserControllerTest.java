package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private static final String PATH_USERS = "/users";
    User user = User.builder()
            .id(1L).name("Ani")
            .email("ani@mail.ru")
            .build();

    UserDtoRequest userDtoRequest = UserDtoRequest.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .build();
    UserDtoResponse userDtoResponse = UserDtoResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .build();

    @SneakyThrows
    @Test
    void addUserTest() throws Exception {
        when(userService.addUser(any(UserDtoRequest.class)))
                .thenReturn(userDtoResponse);

        mockMvc.perform(post(PATH_USERS)
                        .content(mapper.writeValueAsString(userDtoRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDtoResponse.getName())))
                .andExpect(jsonPath("$.email", is(userDtoResponse.getEmail())));
    }

    @SneakyThrows
    @Test
    public void getAllUsersTest() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(userDtoResponse));

        String result = mockMvc.perform(get(PATH_USERS))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(List.of(userDtoResponse)), result);
    }

    @SneakyThrows
    @Test
    public void getUsersByIdTest() throws Exception {
        when(userService.getUser(anyLong())).thenReturn(userDtoResponse);

        mockMvc.perform(get("/users/{id}", userDtoRequest.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDtoResponse.getName())))
                .andExpect(jsonPath("$.email", is(userDtoResponse.getEmail())));
    }

    @SneakyThrows
    @Test
    void updateUserTest() throws Exception {
        when(userService.updateUser(anyLong(), any(UserDtoRequest.class)))
                .thenReturn(userDtoResponse);

        mockMvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDtoRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userDtoResponse)));
    }

    @SneakyThrows
    @Test
    void addUserWhenExceptionTest() throws Exception {
        UserDtoRequest emptyNameUserDto = new UserDtoRequest(1L, "", "test@mail.ru");
        UserDtoRequest invalidEmailUserDto = new UserDtoRequest(1L, "testUser", "testamail.com");
        UserDtoRequest emptyEmailUserDto = new UserDtoRequest(1L, "testUser", "");
        when(userService.addUser(any(UserDtoRequest.class)))
                .thenReturn(userDtoResponse);

        mockMvc.perform(post(PATH_USERS)
                        .content(mapper.writeValueAsString(emptyNameUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(PATH_USERS)
                        .content(mapper.writeValueAsString(invalidEmailUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(PATH_USERS)
                        .content(mapper.writeValueAsString(emptyEmailUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void deleteUserTestTest() throws Exception {
        mockMvc.perform(delete(PATH_USERS + "/1"))
                .andExpect(status().isOk());
        verify(userService, times(1))
                .deleteUser(anyLong());
    }

    @AfterEach
    void deleteUser() {
        userService.deleteUser(anyLong());
    }
}
