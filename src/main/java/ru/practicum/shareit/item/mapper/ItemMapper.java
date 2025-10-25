package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.api.dto.CommentDto;
import ru.practicum.shareit.item.api.dto.CreateItemDto;
import ru.practicum.shareit.item.api.dto.ItemDto;
import ru.practicum.shareit.item.api.dto.ItemWithAdditionalInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.DateMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface ItemMapper {

    Item mapToItem(CreateItemDto createItemDto);

    ItemDto mapToItemDto(Item item);

    @Mapping(source = "item.id", target = "id")
    @Mapping(source = "lastBooking.start", target = "lastBooking.start")
    @Mapping(source = "lastBooking.end", target = "lastBooking.end")
    @Mapping(source = "nextBooking.start", target = "nextBooking.start")
    @Mapping(source = "nextBooking.end", target = "nextBooking.end")
    ItemWithAdditionalInfoDto mapToItemWithAdditionalInfoDto(Item item,
                                                             Booking lastBooking,
                                                             Booking nextBooking,
                                                             List<CommentDto> comments);
}
