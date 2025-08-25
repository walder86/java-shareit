package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private final User user = new User(1L, "name@mail.ru", "name");

    private final UserDto userDto = new UserDto(1L, "name@mail.ru", "name");

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void getUserByIdTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        UserDto userDto = userService.getUserById(user.getId());
        Assertions.assertEquals(userDto.getId(), 1);
    }

    @Test
    void createUserTest() {
        when(userRepository.existsUserByEmail(anyString()))
                .thenReturn(false);
        when(userRepository.save(any()))
                .thenReturn(user);
        UserDto userDtoResult = userService.createUser(userDto);
        Assertions.assertEquals(userDtoResult.getId(), 1);
    }

    @Test
    void updateUserTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any()))
                .thenReturn(user);
        UserDto userDtoResult = userService.updateUser(userDto, user.getId());
        Assertions.assertEquals(userDtoResult.getId(), 1);
    }
}
