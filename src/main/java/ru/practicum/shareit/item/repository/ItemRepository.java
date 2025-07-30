package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    List<Item> getItems();

    List<Item> getItemsByUserId(Long userId);

    Item createItem(Item item);

    Item updateItem(Item item);

    Item getItemById(Long itemId);

    List<Item> searchItemsByText(String text);
}
