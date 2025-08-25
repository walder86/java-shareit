package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.enumerated.State;
import ru.practicum.shareit.booking.enumerated.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemByIdDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookingServiceImpTest {

    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private UserService userService;

    private final BookingDtoRequest bookingDto = new BookingDtoRequest(
            LocalDateTime.now(),
            LocalDateTime.now(),
            1L,
            Status.WAITING
    );

    private final User user = new User(1L, "name@mail.ru", "name");

    private final Item item = new Item(1L, "name", "description", true, user,
            new ItemRequest(1L, "description", user, LocalDateTime.now(), new ArrayList<>()),
            null);

    private final Booking booking = new Booking(
            1L,
            LocalDateTime.now().plusSeconds(5),
            LocalDateTime.now().plusSeconds(60),
            item,
            user,
            Status.WAITING);


    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(bookingRepository, userService, itemService);
    }

    @Test
    void getBookingByIdTest() {
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        BookingDtoResponse bookingDtoResponse = bookingService.getBookingById(user.getId(), 1L);
        Assertions.assertEquals(bookingDtoResponse.getId(), 1);
        Assertions.assertEquals(bookingDtoResponse.getBooker().getName(), "name");
    }

    @Test
    void getBookingByUserTest() {
        when(userService.getUserWithCheck(anyLong()))
                .thenReturn(user);
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong()))
                .thenReturn(List.of(booking));
        List<BookingDtoResponse> bookingDtoResponse = bookingService.getBookingByUser(user.getId(), State.ALL);
        Assertions.assertEquals(bookingDtoResponse.size(), 1);
    }

    @Test
    void getBookingByItemsUserTest() {
        when(userService.getUserWithCheck(anyLong()))
                .thenReturn(user);
        ItemByIdDto itemByIdDto = new ItemByIdDto();
        itemByIdDto.setId(1L);
        when(itemService.getItemsByUserId(anyLong()))
                .thenReturn(List.of(itemByIdDto));
        when(bookingRepository.findAllByItemIdInOrderByStartDesc(anyList()))
                .thenReturn(List.of(booking));
        List<BookingDtoResponse> bookingDtoResponse = bookingService.getBookingByItemsUser(user.getId(), State.ALL);
        Assertions.assertEquals(bookingDtoResponse.size(), 1);
    }

    @Test
    void createBookingTest() {
        when(userService.getUserWithCheck(anyLong()))
                .thenReturn(user);
        when(itemService.getItemWithCheck(anyLong()))
                .thenReturn(item);
        when(bookingRepository.save(any()))
                .thenReturn(booking);
        BookingDtoResponse bookingDtoResponse = bookingService.createBooking(user.getId(), bookingDto);
        Assertions.assertEquals(bookingDtoResponse.getId(), 1L);
    }

    @Test
    void updateBookingTest() {
        when(userService.getUserWithCheck(anyLong()))
                .thenReturn(user);
        when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.save(any()))
                .thenReturn(booking);
        BookingDtoResponse bookingDtoResponse = bookingService.updateBooking(user.getId(), 1L, true);
        Assertions.assertEquals(bookingDtoResponse.getId(), 1L);
    }


}
