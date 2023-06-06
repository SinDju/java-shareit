package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.Valid;
import java.util.Collection;

@Validated
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public Collection<UserDto> getAll() {
        log.info("GET запрос на получение всех пользователей");
        return service.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable long userId) {
        log.info("GET запрос на получение пользователя");
        return service.getUser(userId);
    }

    @Validated({Create.class})
    @PostMapping
    public UserDto addUser(@RequestBody @Valid UserDto userDto) {
        log.info("POST запрос на создание пользователя");
        return service.addUser(userDto);
    }

    @Validated({Update.class})
    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId, @RequestBody @Valid UserDto userDto) {
        log.info("PATCH запрос на обновление пользователя");
        return service.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("DELETE запрос на удаление пользователя с ID: {}", userId);
        service.deleteUser(userId);
    }
}
