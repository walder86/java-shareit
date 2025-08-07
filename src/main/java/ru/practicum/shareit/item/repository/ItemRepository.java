package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    List<Item> getItems();

    List<Item> getItemsByUserId(Long userId);

    Item createItem(Item item);

    Item updateItem(Item item);

    Optional<Item> getItemById(Long itemId);

    List<Item> searchItemsByText(String text);

    Long getNewItemId();
}
