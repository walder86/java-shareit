package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestDbServiceTest {

    private final ItemRequestService itemRequestService;
    private final UserService userService;

    @Test
    void createUserTest() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email");
        UserDto userDtoResult = userService.createUser(userDto);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");
        ItemRequestDto result = Assertions.assertDoesNotThrow(() -> itemRequestService.createItemRequest(itemRequestDto, userDtoResult.getId()));
        Assertions.assertNotNull(result.getId());
    }
}
