package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceDbTest {

    private final UserService userService;

    @Test
    void createUserTest() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("yandex@yandex.ru");
        UserDto result = Assertions.assertDoesNotThrow(() -> userService.createUser(userDto));
        Assertions.assertNotNull(result.getId());
    }
}
