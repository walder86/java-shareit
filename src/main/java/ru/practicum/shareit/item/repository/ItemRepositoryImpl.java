package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private Map<Long, Item> items = new HashMap<>();

    @Override
    public List<Item> getItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> getItemsByUserId(Long userId) {
        return items.values().stream()
                .filter(item -> Objects.equals(item.getOwner().getId(), userId))
                .toList();
    }

    @Override
    public Item createItem(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Optional<Item> getItemById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> searchItemsByText(String text) {
        return items.values().stream()
                .filter(item -> (containsIgnoreCase(item.getName(), text) ||
                        containsIgnoreCase(item.getDescription(), text)) && item.getAvailable())
                .toList();
    }

    private Boolean containsIgnoreCase(String text, String containsText) {
        return text.toLowerCase().contains(containsText.toLowerCase());
    }

    @Override
    public Long getNewItemId() {
        return getItems().stream()
                .map(Item::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }
}
