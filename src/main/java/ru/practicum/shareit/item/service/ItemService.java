package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemByIdDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItemsByUserId(Long userId);

    Item getItemWithCheck(Long itemId);

    ItemDto createItem(ItemDto itemDto, Long userId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId);

    ItemByIdDto getItemById(Long itemId, Long userId);

    List<ItemDto> searchItemsByText(String text);

    CommentDto createComment(Long itemId, Long userId, CommentDto commentDto);
}
