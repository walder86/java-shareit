package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long userId) {
        return getUserByIdWithCheck(userId);
    }

    @Override
    public User createUser(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public void deleteUserById(Long userId) {
        users.remove(userId);
    }

    private User getUserByIdWithCheck(Long userId) {
        User userById = users.get(userId);
        if (userById == null) {
            throw new NotFoundException("Пользователь с ID = " + userId + " не найден.");
        }
        return userById;
    }
}
