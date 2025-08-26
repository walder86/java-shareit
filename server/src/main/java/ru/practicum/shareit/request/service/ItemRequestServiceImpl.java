package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    @Override
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long requesterId) {
        log.info("Создание запроса");
        User user = userService.getUserWithCheck(requesterId);
        ItemRequest newRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(newRequest));
    }

    @Override
    public List<ItemRequestDto> getItemRequestByRequesterId(Long requesterId) {
        log.info("Получение запросов для пользователя с ID = {}", requesterId);
        userService.getUserWithCheck(requesterId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterId(requesterId);
        return itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
    }

    @Override
    public List<ItemRequestDto> getItemRequestOtherUsers(Long userId) {
        log.info("Получение запросов пользователей, кроме пользователя с ID = {}", userId);
        userService.getUserWithCheck(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterIdNot(userId);
        return itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
    }

    @Override
    public ItemRequestDto getItemRequestById(Long userId, Long requestId) {
        log.info("Получение запроса с ID = {} для пользователя с ID = {}", requestId, userId);
        userService.getUserWithCheck(userId);
        ItemRequest itemRequest = getItemRequestWithCheck(requestId);
        return ItemRequestMapper.toItemRequestDto(itemRequest, itemRequest.getItems());
    }

    @Override
    public ItemRequest getItemRequestWithCheck(Long itemRequestId) {
        return itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException("Запрос с ID = " + itemRequestId + " не найден"));
    }
}
