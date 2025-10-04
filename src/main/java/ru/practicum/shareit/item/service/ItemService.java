package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.api.dto.CreateItemDto;
import ru.practicum.shareit.item.api.dto.ItemDto;
import ru.practicum.shareit.item.api.dto.UpdateItemDto;

import java.util.List;


public interface ItemService {
    ItemDto addItem(long userId, CreateItemDto createItemDto);

    ItemDto updateItem(long userId, long itemId, UpdateItemDto updateItemDto);

    ItemDto getItem(long id);

    List<ItemDto> getItems(long userId);

    List<ItemDto> searchItems(String text);
}
