package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public Collection<UserDtoResponse> getAll() {
        log.info("GET запрос на получение всех пользователей");
        return service.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDtoResponse getUser(@PathVariable long userId) {
        log.info("GET запрос на получение пользователя");
        return service.getUser(userId);
    }

    @PostMapping
    public UserDtoResponse addUser(@RequestBody UserDtoRequest userDtoRequest) {
        log.info("POST запрос на создание пользователя");
        return service.addUser(userDtoRequest);
    }

    @PatchMapping("/{userId}")
    public UserDtoResponse updateUser(@PathVariable long userId, @RequestBody UserDtoRequest userDtoRequest) {
        log.info("PATCH запрос на обновление пользователя");
        return service.updateUser(userId, userDtoRequest);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("DELETE запрос на удаление пользователя с ID: {}", userId);
        service.deleteUser(userId);
    }
}
