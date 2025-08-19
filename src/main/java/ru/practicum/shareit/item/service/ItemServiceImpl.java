package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.enumerated.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemByIdDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;


    @Override
    public List<ItemDto> getItemsByUserId(Long userId) {
        log.info("Поиск вещей пользователя с ID = {}", userId);
        User user = userService.getUserWithCheck(userId);
        List<Item> itemsByUserId = itemRepository.findAllByOwner(user);
        return itemsByUserId.stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public Item getItemWithCheck(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID = " + itemId + " не найдена"));
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        log.info("Создание вещи у пользователя с ID = {}", userId);
        User user = userService.getUserWithCheck(userId);
        return ItemMapper.toItemDto(
                itemRepository.save(
                        ItemMapper.toItem(itemDto, user)));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) {
        log.info("Обновление вещи у пользователя с ID = {}", userId);

        Item findItem = getItemWithCheck(itemId);
        checkUser(findItem, userId);
        userService.getUserWithCheck(userId);

        findItem.setName(itemDto.getName() == null || itemDto.getName().isBlank() ?
                findItem.getName() : itemDto.getName());
        findItem.setDescription(itemDto.getDescription() == null || itemDto.getDescription().isBlank() ?
                findItem.getDescription() : itemDto.getDescription());
        findItem.setAvailable(itemDto.getAvailable() != null ? itemDto.getAvailable() : findItem.getAvailable());

        return ItemMapper.toItemDto(itemRepository.save(findItem));
    }

    @Override
    public ItemByIdDto getItemById(Long itemId, Long userId) {
        log.info("Запрос вещи с ID = {}", itemId);
        Item item = getItemWithCheck(itemId);
        User user = userService.getUserWithCheck(userId);
        BookingDtoResponse lastBooking = null;
        BookingDtoResponse nextBooking = null;
        if (item.getOwner().getId().equals(userId)) {
            lastBooking = getLastBookingDtoResponse(itemId, lastBooking, user, item);
            nextBooking = getNextBookingDtoResponse(itemId, nextBooking, user, item);
        }
        return ItemMapper.toItemByIdDto(item, nextBooking, lastBooking);
    }

    private BookingDtoResponse getNextBookingDtoResponse(Long itemId, BookingDtoResponse nextBooking, User user, Item item) {
        Optional<Booking> nextBookingOptional = bookingRepository.findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(
                itemId, LocalDateTime.now(), Status.APPROVED);
        if (nextBookingOptional.isPresent()) {
            nextBooking = BookingMapper.toBookingDtoResponse(
                    nextBookingOptional.get(),
                    UserMapper.toUserDto(user),
                    ItemMapper.toItemDto(item));
        }
        return nextBooking;
    }

    private BookingDtoResponse getLastBookingDtoResponse(Long itemId, BookingDtoResponse lastBooking, User user, Item item) {
        Optional<Booking> lastBookingOptional = bookingRepository.findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(
                itemId, LocalDateTime.now(), Status.APPROVED);
        if (lastBookingOptional.isPresent()) {
            lastBooking = BookingMapper.toBookingDtoResponse(
                    lastBookingOptional.get(),
                    UserMapper.toUserDto(user),
                    ItemMapper.toItemDto(item));
        }
        return lastBooking;
    }

    @Override
    public List<ItemDto> searchItemsByText(String text) {
        log.info("Поиск вещей по тексту \"{}\"", text);
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.findAllByTextIgnoreCaseAndAvailableIsTrue(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public CommentDto createComment(Long itemId, Long userId, CommentDto commentDto) {
        if (!bookingRepository.existsBookingByItemIdAndBookerIdAndStatusAndEndIsBefore(itemId, userId,
                Status.APPROVED, LocalDateTime.now())) {
            throw new ValidationException("У пользователя с ID = " + userId + " не было ни одной брони на вещь с ID = " + itemId);
        }
        User author = userService.getUserWithCheck(userId);
        Item item = getItemWithCheck(itemId);
        Comment comment = ItemMapper.toComment(commentDto, author, item);
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());
        return ItemMapper.toCommentDto(commentRepository.save(comment));
    }

    private void checkUser(Item item, Long userIdFromRequest) {
        if (!Objects.equals(item.getOwner().getId(), userIdFromRequest)) {
            throw new NotAccessException("Вещь с ID = " + item.getId() + " не принадлежит пользователю с ID = " + userIdFromRequest);
        }
    }

}
