package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

    @ExtendWith(MockitoExtension.class)
    public class UserServiceUnitTest {
        @InjectMocks
        private UserServiceImpl userService;
        @Mock
        private UserRepository repository;

        private final User user = new User(1L, "Ask", "ask@mail.ru");
        private final UserDtoRequest userDtoRequest = new UserDtoRequest(1L, "Ask", "ask@mail.ru");
        private final UserDtoResponse userDtoResponse = new UserDtoResponse(1L, "Ask", "ask@mail.ru");
        private final User user2 = new User(2L,  "Miy", "miy@yandex.ru");
        private final UserDtoRequest userDtoRequest2 = new UserDtoRequest(2L, "Miy", "miy@yandex.ru");
        private final UserDtoResponse userDtoResponse2 = new UserDtoResponse(2L, "Miy", "miy@yandex.ru");


        @Test
        public void createUserTest() {
            when(repository.save(any()))
                    .thenReturn(user);

            assertThat(userService.addUser(userDtoRequest), equalTo(userDtoResponse));
        }

        @Test
        public void getUserByIdExistTest() {
            when(repository.findById(anyLong()))
                    .thenReturn(Optional.of(user));

            assertThat(userDtoResponse, equalTo(userService.getUser(1L)));
        }

        @Test
        public void getUserByIdNotExistTest() {
            when(repository.findById(anyLong()))
                    .thenReturn(empty());

            Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                    () -> userService.getUser(1L));
            assertThat(exception.getMessage(), equalTo("Пользователь с ID " +
                    1 + " не зарегистрирован!"));
        }

        @Test
        public void getAllUsersTest() {
            when(repository.findAll())
                    .thenReturn(List.of(user, user2));
            Collection<UserDtoResponse> users = userService.getAllUsers();

            assertThat(users, equalTo(List.of(userDtoResponse, userDtoResponse2)));
        }

        @Test
        public void findByIdThrowNotFoundExceptionTest() {
            when(repository.findById(anyLong()))
                    .thenReturn(empty());

            Exception exception = Assertions.assertThrows(ObjectNotFoundException.class,
                    () -> userService.getUser(1L));
            assertThat(exception.getMessage(), equalTo("Пользователь с ID " +
                    1 + " не зарегистрирован!"));
        }

        @Test
        public void deleteUserTest() {
            userService.deleteUser(anyLong());
            verify(repository, times(1)).deleteById(anyLong());
        }

        @Test
        public void testToUserDto() {
            User user = new User(1L, "Professor Oak", "ProfessorOak@gmail.com");

            UserDtoResponse userDto = UserMapper.toUserDtoResponse(user);
            assertEquals(1L, userDto.getId());
            assertEquals("Professor Oak", userDto.getName());
            assertEquals("ProfessorOak@gmail.com", userDto.getEmail());

            User user2 = UserMapper.toUserModel(userDtoRequest2);
            assertEquals(2L, user2.getId());
            assertEquals("Miy", user2.getName());
            assertEquals("miy@yandex.ru", user2.getEmail());
        }
    }
