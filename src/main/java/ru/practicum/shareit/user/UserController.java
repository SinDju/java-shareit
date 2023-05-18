package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public Collection<User> getAll() {
        log.info("GET запрос на получение всех пользователей");
        return service.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable long userId) {
        log.info("GET запрос на получение пользователя");
        return service.getUser(userId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("POST запрос на создание пользователя");
        return service.addUser(user);
    }

    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable long userId, @RequestBody User user) {
        log.info("PATCH запрос на обновление пользователя");
        return service.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("DELETE запрос на удаление пользователя с ID: {}", userId);
        service.deleteUser(userId);
    }
}
