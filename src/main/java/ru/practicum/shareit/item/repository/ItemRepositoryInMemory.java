package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class ItemRepositoryInMemory implements ItemRepository {

    private final Map<Long, Item> items;
    private long count;

    public ItemRepositoryInMemory() {
        this.items = new HashMap<>();
        this.count = 0;
    }

    private long getNextItemId() {
        return ++count;
    }

    @Override
    public Item addItem(Item item) {
        item.setId(getNextItemId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> getItem(long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> getItems(long userId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItems(String text) {
        return items.values()
                .stream()
                .filter(Item::isAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text))
                .collect(Collectors.toList());
    }

    @Override
    public void updateItem(Item item) {
        items.put(item.getId(), item);
    }
}
