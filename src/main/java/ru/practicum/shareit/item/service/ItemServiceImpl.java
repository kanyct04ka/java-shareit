package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.api.ItemDtoMapper;
import ru.practicum.shareit.item.api.dto.CreateItemDto;
import ru.practicum.shareit.item.api.dto.ItemDto;
import ru.practicum.shareit.item.api.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;


@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository,
                           UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto addItem(long userId, CreateItemDto createItemDto) {
        User user = userRepository.getUser(userId)
                .orElseThrow(() -> new EntityNotFoundException("user-owner not found"));

        Item item = ItemDtoMapper.mapToItem(createItemDto);
        item.setOwner(user);

        return ItemDtoMapper.mapToItemDto(itemRepository.addItem(item));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, UpdateItemDto updateItemDto) {
        User user = userRepository.getUser(userId)
// ?? тест требует для прохождения 404, но логически больше подходит 400 и BadRequestException ??
                .orElseThrow(() -> new EntityNotFoundException("user-owner not found"));

        Item item = itemRepository.getItem(itemId)
                .orElseThrow(() -> new EntityNotFoundException("item not found"));

        if (!item.getOwner().equals(user)) {
            throw new BadRequestException("trying to update foreign item");
        }

        if (updateItemDto.getName() != null && !updateItemDto.getName().isBlank()) {
            item.setName(updateItemDto.getName());
        }

        if (updateItemDto.getDescription() != null) {
            item.setDescription(updateItemDto.getDescription());
        }

        if (updateItemDto.getAvailable() != null) {
            item.setAvailable(updateItemDto.getAvailable());
        }

        itemRepository.updateItem(item);
        return ItemDtoMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto getItem(long id) {
        Item item = itemRepository.getItem(id)
                .orElseThrow(() -> new EntityNotFoundException("item not found"));
        return ItemDtoMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemDto> getItems(long userId) {
        return itemRepository.getItems(userId)
                .stream()
                .map(ItemDtoMapper::mapToItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.getItems(text.toLowerCase())
                .stream()
                .map(ItemDtoMapper::mapToItemDto)
                .toList();
    }
}
