package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @Valid @RequestBody ItemDto itemDto) {
        log.info("Создание вещи с параметрами: itemDto={}, userId={}", itemDto, userId);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestBody ItemDto itemDto,
                                                   @PathVariable Long itemId) {
        log.info("Обновление вещи с параметрами: itemDto={}, userId={}, itemId={}", itemDto, userId, itemId);
        return itemClient.updateItem(userId, itemDto, itemId);
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @PathVariable Long itemId) {
        log.info("Получение вещи с параметрами: userId={}, itemId={}", userId, itemId);
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByUserId(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение вещей пользователя с параметрами: userId={}", userId);
        return itemClient.getItemsByUserId(userId);
    }

    @GetMapping("search")
    public ResponseEntity<Object> getItemsSearchByText(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam String text) {
        log.info("Поиск вещей по тексту с параметрами: userId={}, text={}", userId, text);
        return itemClient.getItemsSearchByText(userId, text);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Long itemId,
                                                   @RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @Valid @RequestBody CommentDto commentDto) {
        log.info("Создание комментария к вещи с параметрами: userId={}, itemId={}, commentDto={}", userId, itemId, commentDto);
        return itemClient.createComment(itemId, userId, commentDto);
    }

}
