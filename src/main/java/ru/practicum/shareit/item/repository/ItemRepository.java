package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import java.util.List;
import java.util.Optional;


public interface ItemRepository {
    Item addItem(Item item);

    Optional<Item> getItem(long id);

    List<Item> getItems(long userId);

    List<Item> getItems(String text);

    void updateItem(Item item);
}
