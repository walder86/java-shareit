package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistException;
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
        User userById = userRepository.getUserById(userId);
        return UserMapper.toUserDto(userById);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Создание пользователя");
        checkEmail(userDto.getEmail());
        return UserMapper.toUserDto(userRepository.createUser(UserMapper.toUser(userDto, getNewUserId())));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        log.info("Обновление пользователя с ID = {}", userId);
        User findUser = userRepository.getUserById(userId);
        if (userDto.getEmail() != null) checkEmail(userDto.getEmail());
        findUser.setEmail(userDto.getEmail() != null ? userDto.getEmail() : findUser.getEmail());
        findUser.setName(userDto.getName() != null ? userDto.getName() : findUser.getName());
        return UserMapper.toUserDto(userRepository.updateUser(findUser));
    }

    @Override
    public void deleteUserById(Long userId) {
        log.info("Удаление пользователя с ID = {}", userId);
        userRepository.deleteUserById(userId);
    }

    private Long getNewUserId() {
        return userRepository.getUsers().stream()
                .map(User::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    private void checkEmail(String email) {
        if (userRepository.getUsers().stream()
                .map(User::getEmail)
                .anyMatch(email::equals)) {
            throw new AlreadyExistException("Пользователь с почтой " + email + " уже существует");
        }
    }
}
