package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceImplTest {
    private final EntityManager entityManager;
    private final UserService userService;
    UserDtoRequest userDto;

    @BeforeEach
    void toStart() {
        userDto = new UserDtoRequest(1L, "Sakura", "sakura@mail.ru");
    }

    @Test
    void addUser() {
        userService.addUser(userDto);
        TypedQuery<User> query = entityManager
                .createQuery("Select u from User u where u.email = :email", User.class);
        User user = query
                .setParameter("email", userDto.getEmail())
                .getSingleResult();

        assertNotNull(user);
        assertThat(user.getId(), equalTo(1L));
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void getUserById() {
        userService.addUser(userDto);
        UserDtoResponse user = userService.getUser(1L);

        assertThat(user.getId(), equalTo(1L));
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void updateUser() {
        UserDtoRequest userToCreate = new UserDtoRequest(1L, "ash@gmail.com", "Ash");
        UserDtoRequest userToUpdate = new UserDtoRequest(1L, "Update1ash@gmail.com", "AshUpdate1");
        UserDtoRequest userToUpdate2 = new UserDtoRequest(1L, "Update2ash@gmail.com", "AshUpdate2");
        UserDtoRequest userToUpdate3 = new UserDtoRequest(1L, "Update3ash@gmail.com", "AshUpdate3");
        userService.addUser(userToCreate);
        UserDtoResponse updatedUser = userService.updateUser(1L,userToUpdate);

        assertThat(updatedUser.getName(), equalTo(userToUpdate.getName()));
        assertThat(updatedUser.getEmail(), equalTo(userToUpdate.getEmail()));

        updatedUser = userService.updateUser(1L, userToUpdate2);

        assertThat(updatedUser.getEmail(), equalTo(userToUpdate2.getEmail()));

        updatedUser = userService.updateUser(1L, userToUpdate3);

        assertThat(updatedUser.getName(), equalTo(userToUpdate3.getName()));
    }

    @Test
    void deleteUser() {
        userService.addUser(userDto);
        userService.deleteUser(1L);

        Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.getUser(1L));
    }
}
