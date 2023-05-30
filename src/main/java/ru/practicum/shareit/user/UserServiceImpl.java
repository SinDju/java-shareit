package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.ObjectNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserRepository repository;

    @Override
    public Collection<User> getAllUsers() {
       // return userDao.getAll();
        return repository.findAll();
    }

    @Override
    public User getUser(long userId) {
        User user = repository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь с ID " +
                        userId + " не зарегистрирован!"));
        return user;
    }

    @Override
    public User addUser(User user) {
        List<User> userList = getAllUsers().stream().collect(Collectors.toList());
        if (userList.contains(user)) {
            throw new DuplicateException();
        }
        return repository.save(user);
    }

    @Override
    public User updateUser(long userId, User user) {
        List<User> userList = getAllUsers().stream().collect(Collectors.toList());
        userList.remove(getUser(userId));
        for (User user1 : userList) {
            if (user1.getEmail().equals(user.getEmail())) {
                throw new DuplicateException();
            }
        }
        return userDao.updateUser(userId, user);
    }

    @Override
    public void deleteUser(long userId) {
        repository.deleteById(userId);
    }
}
