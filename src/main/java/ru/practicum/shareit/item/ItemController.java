package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemByIdDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final String userIdHeader = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDto> getItemsByUserId(@RequestHeader(userIdHeader) Long userId) {
        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemByIdDto getItemById(@PathVariable Long itemId, @RequestHeader(userIdHeader) Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @PostMapping
    public ItemDto createItem(
            @Valid @RequestBody ItemDto itemDto,
            @RequestHeader(userIdHeader) Long userId) {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestBody ItemDto itemDto,
            @PathVariable Long itemId,
            @RequestHeader(userIdHeader) Long userId) {
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsSearchByText(@RequestParam String text) {
        return itemService.searchItemsByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createCommet(
            @RequestBody CommentDto commentDto,
            @PathVariable Long itemId,
            @RequestHeader(userIdHeader) Long userId) {
        return itemService.createComment(itemId, userId, commentDto);
    }
}
