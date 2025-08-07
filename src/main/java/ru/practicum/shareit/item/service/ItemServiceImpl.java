package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotAccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @Override
    public List<ItemDto> getItemsByUserId(Long userId) {
        log.info("Поиск вещей пользователя с ID = {}", userId);
        userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + userId + " не найден"));
        List<Item> itemsByUserId = itemRepository.getItemsByUserId(userId);
        return itemsByUserId.stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        log.info("Создание вещи у пользователя с ID = {}", userId);
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + userId + " не найден"));
        return ItemMapper.toItemDto(
                itemRepository.createItem(
                        ItemMapper.toItem(
                                itemRepository.getNewItemId(), itemDto, user, null)));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) {
        log.info("Обновление вещи у пользователя с ID = {}", userId);
        Item findItem = itemRepository.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID = " + itemId + " не найдена"));
        checkUser(findItem, userId);
        userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + userId + " не найден"));
        findItem.setName(itemDto.getName() == null || itemDto.getName().isBlank() ?
                findItem.getName() : itemDto.getName());
        findItem.setDescription(itemDto.getDescription() == null || itemDto.getDescription().isBlank() ?
                findItem.getDescription() : itemDto.getDescription());
        findItem.setAvailable(itemDto.getAvailable() != null ? itemDto.getAvailable() : findItem.getAvailable());
        return ItemMapper.toItemDto(itemRepository.updateItem(findItem));
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        log.info("Запрос вещи с ID = {}", itemId);
        Item item = itemRepository.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID = " + itemId + " не найдена"));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> searchItemsByText(String text) {
        log.info("Поиск вещей по тексту \"{}\"", text);
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.searchItemsByText(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    private void checkUser(Item item, Long userIdFromRequest) {
        if (!Objects.equals(item.getOwner().getId(), userIdFromRequest)) {
            throw new NotAccessException("Вещь с ID = " + item.getId() + " не принадлежит пользователю с ID = " + userIdFromRequest);
        }
    }

}
