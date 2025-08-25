package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemRequestServiceTest {

    private ItemRequestService itemRequestService;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserService userService;

    private final User user = new User(1L, "name@mail.ru", "name");

    private final ItemRequestDto itemRequestDtoForRequest = new ItemRequestDto(
            1L,
            "description",
            LocalDateTime.now(),
            new ArrayList<>()
    );

    private final Item item = new Item(1L, "name", "description", true, user,
            new ItemRequest(1L, "description", user, LocalDateTime.now(), new ArrayList<>()),
            null);

    private final ItemRequest itemRequest = new ItemRequest(
            1L,
            "description",
            user,
            LocalDateTime.now(),
            null
    );

    private final ItemRequest itemRequestWithItems = new ItemRequest(
            1L,
            "description",
            user,
            LocalDateTime.now(),
            List.of(item)
    );

    @BeforeEach
    void setUp() {
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userService);
    }

    @Test
    void createItemRequestTest() {
        when(userService.getUserWithCheck(anyLong()))
                .thenReturn(user);
        when(itemRequestRepository.save(any()))
                .thenReturn(itemRequest);
        ItemRequestDto itemRequestResult = itemRequestService.createItemRequest(itemRequestDtoForRequest, user.getId());
        Assertions.assertEquals(itemRequestResult.getItems().size(), 0);
    }

    @Test
    void getItemRequestByRequesterIdTest() {
        when(userService.getUserWithCheck(anyLong()))
                .thenReturn(user);
        when(itemRequestRepository.findAllByRequesterId(anyLong()))
                .thenReturn(List.of(itemRequest));
        List<ItemRequestDto> itemRequestResult = itemRequestService.getItemRequestByRequesterId(user.getId());
        Assertions.assertEquals(itemRequestResult.size(), 1);
    }

    @Test
    void getItemRequestByIdTest() {
        when(userService.getUserWithCheck(anyLong()))
                .thenReturn(user);
        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemRequestWithItems));
        ItemRequestDto itemRequestResult = itemRequestService.getItemRequestById(user.getId(), itemRequest.getId());
        Assertions.assertEquals(itemRequestResult.getItems().size(), 1);
    }
}
