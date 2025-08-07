package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {

    UserDto getUserById(Long userId);

    User getUserWithCheck(Long userId);

    UserDto createUser(UserDto user);

    UserDto updateUser(UserDto user, Long userId);

    void deleteUserById(Long userId);
}
