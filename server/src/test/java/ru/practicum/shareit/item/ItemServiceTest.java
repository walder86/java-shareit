package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.enumerated.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemByIdDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemServiceTest {

    private ItemService itemService;

    @Mock
    private ItemRequestService itemRequestService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    private final User user = new User(1L, "name@mail.ru", "name");

    private final Item item = new Item(1L, "name", "description", true, user,
            new ItemRequest(1L, "description", user, LocalDateTime.now(), new ArrayList<>()),
            null);

    private final CommentDto commentDto = new CommentDto(1L, "text", "authoe name", LocalDateTime.now());

    private final Comment comment = new Comment(1L, "text", item, user, LocalDateTime.now());

    private final Booking booking = new Booking(
            1L,
            LocalDateTime.now().plusSeconds(5),
            LocalDateTime.now().plusSeconds(60),
            item,
            user,
            Status.APPROVED);

    private final ItemDto itemDto = new ItemDto(
            1L,
            "name",
            "description",
            true,
            null,
            new ArrayList<>()
    );

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRequestService, itemRepository, userService, bookingRepository, commentRepository);
    }

    @Test
    void getItemsByUserIdTest() {
        when(userService.getUserWithCheck(anyLong()))
                .thenReturn(user);
        when(itemRepository.findAllByOwner(any()))
                .thenReturn(List.of(item));
        when(bookingRepository.findAllByItemIdInOrderByStartDesc(anyList()))
                .thenReturn(List.of(booking));
        List<ItemByIdDto> itemsByUserId = itemService.getItemsByUserId(user.getId());
        Assertions.assertEquals(itemsByUserId.size(), 1);
    }

    @Test
    void createItemTest() {
        when(userService.getUserWithCheck(anyLong()))
                .thenReturn(user);
        when(itemRepository.save(any()))
                .thenReturn(item);
        ItemDto item = itemService.createItem(itemDto, user.getId());
        Assertions.assertEquals(item.getId(), 1);
    }

    @Test
    void updateItemTest() {
        when(userService.getUserWithCheck(anyLong()))
                .thenReturn(user);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(itemRepository.save(any()))
                .thenReturn(item);
        ItemDto itemResult = itemService.updateItem(itemDto, item.getId(), user.getId());
        Assertions.assertEquals(itemResult.getId(), 1);
    }

    @Test
    void getItemByIdTest() {
        when(userService.getUserWithCheck(anyLong()))
                .thenReturn(user);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(anyLong(), any(), any()))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(anyLong(), any(), any()))
                .thenReturn(Optional.of(booking));
        when(itemRepository.save(any()))
                .thenReturn(item);
        ItemByIdDto itemByIdDto = itemService.getItemById(item.getId(), user.getId());
        Assertions.assertEquals(itemByIdDto.getId(), 1);
        Assertions.assertNotNull(itemByIdDto.getNextBooking());
        Assertions.assertNotNull(itemByIdDto.getLastBooking());
    }

    @Test
    void searchItemsByTextTest() {
        when(userService.getUserWithCheck(anyLong()))
                .thenReturn(user);
        when(itemRepository.findAllByTextIgnoreCaseAndAvailableIsTrue(anyString()))
                .thenReturn(List.of(item));
        List<ItemDto> itemDto = itemService.searchItemsByText("text");
        Assertions.assertEquals(itemDto.size(), 1);
    }

    @Test
    void createCommentTest() {
        when(userService.getUserWithCheck(anyLong()))
                .thenReturn(user);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(bookingRepository.existsBookingByItemIdAndBookerIdAndStatusAndEndIsBefore(anyLong(), anyLong(), any(), any()))
                .thenReturn(true);
        when(commentRepository.save(any()))
                .thenReturn(comment);
        CommentDto commentDtoResult = itemService.createComment(item.getId(), user.getId(), commentDto);
        Assertions.assertEquals(commentDtoResult.getId(), 1);
    }
}
