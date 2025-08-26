package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long requesterId);

    List<ItemRequestDto> getItemRequestByRequesterId(Long requesterId);

    List<ItemRequestDto> getItemRequestOtherUsers(Long requesterId);

    ItemRequestDto getItemRequestById(Long userId, Long requestId);

    ItemRequest getItemRequestWithCheck(Long itemRequestId);
}
