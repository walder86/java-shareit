package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.enumerated.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingDbServiceTest {

    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    @Test
    void updateBookingTest() {
        UserDto userDto = new UserDto();
        userDto.setName("name");
        userDto.setEmail("email@email.ru");
        UserDto userDtoResult = userService.createUser(userDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        ItemDto itemDtoResult = itemService.createItem(itemDto, userDtoResult.getId());

        BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
        bookingDtoRequest.setStart(LocalDateTime.now().plusSeconds(5));
        bookingDtoRequest.setEnd(LocalDateTime.now().plusSeconds(60));
        bookingDtoRequest.setItemId(itemDtoResult.getId());
        BookingDtoResponse bookingDtoResponseCreated = Assertions.assertDoesNotThrow(
                () -> bookingService.createBooking(userDtoResult.getId(), bookingDtoRequest));
        Assertions.assertNotNull(bookingDtoResponseCreated.getId());
        Assertions.assertEquals(bookingDtoResponseCreated.getStatus(), Status.WAITING);

        BookingDtoResponse result = Assertions.assertDoesNotThrow(
                () -> bookingService.updateBooking(userDtoResult.getId(), bookingDtoResponseCreated.getId(), true));
        Assertions.assertEquals(result.getStatus(), Status.APPROVED);
    }
}
