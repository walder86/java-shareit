package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final String userIdHeader = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader(userIdHeader) Long requesterId,
                                        @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.createItemRequest(itemRequestDto, requesterId);
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestByRequesterId(
            @RequestHeader(userIdHeader) Long requesterId) {
        return itemRequestService
                .getItemRequestByRequesterId(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getItemRequestOtherUsers(
            @RequestHeader(userIdHeader) Long userId) {
        return itemRequestService.getItemRequestOtherUsers(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequest(
            @RequestHeader(userIdHeader) Long userId,
            @PathVariable Long requestId) {
        return itemRequestService.getItemRequestById(userId, requestId);
    }

}
