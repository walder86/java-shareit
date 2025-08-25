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

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    @Override
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long requesterId) {
        User user = userService.getUserWithCheck(requesterId);
        ItemRequest newRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);
        newRequest.setRequester(user);
        newRequest.setCreated(LocalDateTime.now());
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(newRequest));
    }

    @Override
    public List<ItemRequestDto> getItemRequestByRequesterId(Long requesterId) {
        userService.getUserWithCheck(requesterId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequesterId(requesterId);
        return itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
    }

    @Override
    public ItemRequestDto getItemRequestById(Long userId, Long requestId) {
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
