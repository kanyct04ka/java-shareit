package ru.practicum.shareit.item.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.api.dto.CreateItemDto;
import ru.practicum.shareit.item.api.dto.ItemDto;
import ru.practicum.shareit.item.api.dto.UpdateItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;


@Slf4j
@Validated
@RestController
@RequestMapping(path = "/items")
public class ItemController {

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(
            @RequestHeader("X-Sharer-User-Id") @Positive
            long userId,
            @RequestBody @Valid
            CreateItemDto item
    ) {
        log.info("Request from user_id={} to add item: {}", userId, item.toString());
        return itemService.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @PathVariable @Positive(message = "item id should be positive number")
            long itemId,
            @RequestBody @Valid
            UpdateItemDto item
    ) {
        log.info("Request from user_id={} to update item with id={}", userId, itemId);
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping
    public List<ItemDto> getUserItems(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId
    ) {
        return itemService.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @PathVariable @Positive(message = "item id should be positive number")
            long itemId
    ) {
        return itemService.getItem(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(
            @RequestHeader("X-Sharer-User-Id") @Positive(message = "user id should be positive number")
            long userId,
            @RequestParam
            String text
    ) {
        return itemService.searchItems(text);
    }
}
