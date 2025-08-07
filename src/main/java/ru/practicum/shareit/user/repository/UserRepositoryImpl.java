package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(users.get(userId));
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

    @Override
    public Long getNewUserId() {
        return getUsers().stream()
                .map(User::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    @Override
    public void checkEmail(String email) {
        if (getUsers().stream()
                .map(User::getEmail)
                .anyMatch(email::equals)) {
            throw new AlreadyExistException("Пользователь с почтой " + email + " уже существует");
        }
    }
}
