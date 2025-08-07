package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto getUserById(Long userId) {
        log.info("Запрос пользователя с ID = {}", userId);
        User userById = userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + userId + " не найден"));
        return UserMapper.toUserDto(userById);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Создание пользователя");
        userRepository.checkEmail(userDto.getEmail());
        return UserMapper.toUserDto(
                userRepository.createUser(
                        UserMapper.toUser(
                                userDto, userRepository.getNewUserId())));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        log.info("Обновление пользователя с ID = {}", userId);
        User findUser = userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + userId + " не найден"));
        if (userDto.getEmail() != null && !userDto.getEmail().equals(findUser.getEmail())) {
            userRepository.checkEmail(userDto.getEmail());
        }
        findUser.setEmail(userDto.getEmail() == null || userDto.getEmail().isBlank() ?
                findUser.getEmail() : userDto.getEmail());
        findUser.setName(userDto.getName() == null || userDto.getName().isBlank() ?
                findUser.getName() : userDto.getName());
        return UserMapper.toUserDto(userRepository.updateUser(findUser));
    }

    @Override
    public void deleteUserById(Long userId) {
        log.info("Удаление пользователя с ID = {}", userId);
        userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + userId + " не найден"));
        userRepository.deleteUserById(userId);
    }
}
