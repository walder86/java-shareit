package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemDbServiceTest {

    private final ItemService itemService;
    private final UserService userService;

    @Test
    void createItemTest() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@yandex.ru");
        UserDto userDtoResult = userService.createUser(userDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        ItemDto result = Assertions.assertDoesNotThrow(() -> itemService.createItem(itemDto, userDtoResult.getId()));
        Assertions.assertNotNull(result.getId());
    }
}
