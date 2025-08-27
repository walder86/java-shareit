package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") Long requesterId,
                                                @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Создание запроса с параметрами: requesterId={}, itemRequestDto={}", requesterId, itemRequestDto);
        return itemRequestClient.createRequest(requesterId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestByRequesterId(
            @RequestHeader("X-Sharer-User-Id") Long requesterId) {
        log.info("Получение запросов с параметрами: requesterId={}", requesterId);
        return itemRequestClient.getItemRequestByRequesterId(requesterId);
    }

    @GetMapping("all")
    public ResponseEntity<Object> getItemRequestOtherUsers(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение запросов с параметрами: userId={}", userId);
        return itemRequestClient.getItemRequestOtherUsers(userId);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getItemRequestById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId) {
        log.info("Получение запроса с параметрами: userId={}, requestId={}", userId, requestId);
        return itemRequestClient.getItemRequestById(userId, requestId);
    }
}