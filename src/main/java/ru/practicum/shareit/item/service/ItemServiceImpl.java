package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.api.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;


    @Override
    public ItemDto addItem(long userId, CreateItemDto createItemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user-owner not found"));

        Item item = itemMapper.mapToItem(createItemDto);
        item.setOwner(user);
        item.setCreated(Instant.now());
        item.setUpdated(Instant.now());

        return itemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, UpdateItemDto updateItemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user-owner not found"));

        Item item = itemRepository.findById(itemId)
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

        item.setUpdated(Instant.now());
        itemRepository.save(item);

        return itemMapper.mapToItemDto(item);
    }

    @Override
    public ItemWithAdditionalInfoDto getItem(long itemId, long userId) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("item not found"));

        List<CommentDto>  comments = commentRepository.findAllByItemId(itemId)
                .stream()
                .map(commentMapper::mapToCommentDto)
                .collect(Collectors.toList());

        if (item.getOwner().getId() == userId) {
            return itemMapper.mapToItemWithAdditionalInfoDto(item,
                    bookingRepository.findLastBookingForItem(itemId, Instant.now()),
                    bookingRepository.findNextBookingForItem(itemId, Instant.now()),
                    comments);
        }

        return itemMapper.mapToItemWithAdditionalInfoDto(item, null, null, comments);
    }

    @Override
    public List<ItemDto> getItems(long userId) {
        return itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(itemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.findByNameContainingOrDescriptionContaining(text.toLowerCase())
                .stream()
                .map(itemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public CommentDto addComment(long userId, long itemId, CreateCommentDto createComment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user-owner not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("item not found"));

        if (bookingRepository.findFirstByItemIdAndUserIdAndStatusAndEndBefore(itemId, userId, BookingStatus.APPROVED, Instant.now())
                .isEmpty()) {
            throw new BadRequestException("you can add comment only for items that booked before");
        }

        return commentMapper.mapToCommentDto(commentRepository.save(
                Comment.builder()
                .author(user)
                .item(item)
                .text(createComment.getText())
                .created(Instant.now())
                .build())
        );
    }

}
