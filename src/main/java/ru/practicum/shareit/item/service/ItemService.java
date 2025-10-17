package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.api.dto.*;
import java.util.List;


public interface ItemService {
    ItemDto addItem(long userId, CreateItemDto createItemDto);

    ItemDto updateItem(long userId, long itemId, UpdateItemDto updateItemDto);

    ItemWithAdditionalInfoDto getItem(long id);

    List<ItemDto> getItems(long userId);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(long userId, long itemId, CreateCommentDto comment);
}
