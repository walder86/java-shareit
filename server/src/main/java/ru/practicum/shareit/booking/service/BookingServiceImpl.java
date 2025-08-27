package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.enumerated.State;
import ru.practicum.shareit.booking.enumerated.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemByIdDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;


    @Override
    public BookingDtoResponse getBookingById(Long userId, Long bookingId) {
        log.info("Запрос бронирования с ID = " + bookingId);
        Booking booking = getBookingWithCheck(bookingId);
        checkBookingUserForGet(userId, booking.getBooker().getId(), booking.getItem().getOwner().getId());
        return BookingMapper.toBookingDtoResponse(
                booking,
                UserMapper.toUserDto(booking.getBooker()),
                ItemMapper.toItemDto(booking.getItem()));
    }

    @Override
    public List<BookingDtoResponse> getBookingByUser(Long bookerId, State state) {
        log.info("Запрос всех бронирования пользователя с ID = " + bookerId);
        userService.getUserWithCheck(bookerId);
        return switch (state) {
            case ALL -> bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId).stream()
                    .map(booking -> BookingMapper.toBookingDtoResponse(
                            booking,
                            UserMapper.toUserDto(booking.getBooker()),
                            ItemMapper.toItemDto(booking.getItem())))
                    .toList();
            case CURRENT ->
                    bookingRepository.findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(bookerId, LocalDateTime.now(), LocalDateTime.now()).stream()
                            .map(booking -> BookingMapper.toBookingDtoResponse(
                                    booking,
                                    UserMapper.toUserDto(booking.getBooker()),
                                    ItemMapper.toItemDto(booking.getItem())))
                            .toList();
            case PAST ->
                    bookingRepository.findAllByBookerIdAndEndIsBeforeOrderByStartDesc(bookerId, LocalDateTime.now()).stream()
                            .map(booking -> BookingMapper.toBookingDtoResponse(
                                    booking,
                                    UserMapper.toUserDto(booking.getBooker()),
                                    ItemMapper.toItemDto(booking.getItem())))
                            .toList();
            case FUTURE ->
                    bookingRepository.findAllByBookerIdAndStartIsAfterOrderByStartDesc(bookerId, LocalDateTime.now()).stream()
                            .map(booking -> BookingMapper.toBookingDtoResponse(
                                    booking,
                                    UserMapper.toUserDto(booking.getBooker()),
                                    ItemMapper.toItemDto(booking.getItem())))
                            .toList();
            case WAITING ->
                    bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId, Status.WAITING).stream()
                            .map(booking -> BookingMapper.toBookingDtoResponse(
                                    booking,
                                    UserMapper.toUserDto(booking.getBooker()),
                                    ItemMapper.toItemDto(booking.getItem())))
                            .toList();
            case REJECTED ->
                    bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(bookerId, Status.REJECTED).stream()
                            .map(booking -> BookingMapper.toBookingDtoResponse(
                                    booking,
                                    UserMapper.toUserDto(booking.getBooker()),
                                    ItemMapper.toItemDto(booking.getItem())))
                            .toList();
            default -> throw new ValidationException("Недоступное состояние: " + state);
        };

    }

    @Override
    public List<BookingDtoResponse> getBookingByItemsUser(Long userOwnerItemId, State state) {
        log.info("Запрос всех забронированных вещей пользователя с ID = " + userOwnerItemId);
        userService.getUserWithCheck(userOwnerItemId);
        List<Long> itemIds = itemService.getItemsByUserId(userOwnerItemId).stream().map(ItemByIdDto::getId).toList();
        return switch (state) {
            case ALL -> bookingRepository.findAllByItemIdInOrderByStartDesc(itemIds).stream()
                    .map(booking -> BookingMapper.toBookingDtoResponse(
                            booking,
                            UserMapper.toUserDto(booking.getBooker()),
                            ItemMapper.toItemDto(booking.getItem())))
                    .toList();
            case CURRENT ->
                    bookingRepository.findAllByItemIdInAndStartIsBeforeAndEndIsAfterOrderByStartDesc(itemIds, LocalDateTime.now(), LocalDateTime.now()).stream()
                            .map(booking -> BookingMapper.toBookingDtoResponse(
                                    booking,
                                    UserMapper.toUserDto(booking.getBooker()),
                                    ItemMapper.toItemDto(booking.getItem())))
                            .toList();
            case PAST ->
                    bookingRepository.findAllByItemIdInAndEndIsBeforeOrderByStartDesc(itemIds, LocalDateTime.now()).stream()
                            .map(booking -> BookingMapper.toBookingDtoResponse(
                                    booking,
                                    UserMapper.toUserDto(booking.getBooker()),
                                    ItemMapper.toItemDto(booking.getItem())))
                            .toList();
            case FUTURE ->
                    bookingRepository.findAllByItemIdInAndStartIsAfterOrderByStartDesc(itemIds, LocalDateTime.now()).stream()
                            .map(booking -> BookingMapper.toBookingDtoResponse(
                                    booking,
                                    UserMapper.toUserDto(booking.getBooker()),
                                    ItemMapper.toItemDto(booking.getItem())))
                            .toList();
            case WAITING ->
                    bookingRepository.findAllByItemIdInAndStatusOrderByStartDesc(itemIds, Status.WAITING).stream()
                            .map(booking -> BookingMapper.toBookingDtoResponse(
                                    booking,
                                    UserMapper.toUserDto(booking.getBooker()),
                                    ItemMapper.toItemDto(booking.getItem())))
                            .toList();
            case REJECTED ->
                    bookingRepository.findAllByItemIdInAndStatusOrderByStartDesc(itemIds, Status.REJECTED).stream()
                            .map(booking -> BookingMapper.toBookingDtoResponse(
                                    booking,
                                    UserMapper.toUserDto(booking.getBooker()),
                                    ItemMapper.toItemDto(booking.getItem())))
                            .toList();
            default -> throw new ValidationException("Недоступное состояние: " + state);
        };
    }

    @Override
    public BookingDtoResponse createBooking(Long userId, BookingDtoRequest bookingDtoRequest) {
        log.info("Создание бронирования");
        User user = userService.getUserWithCheck(userId);
        Item item = itemService.getItemWithCheck(bookingDtoRequest.getItemId());
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь с ID = " + bookingDtoRequest.getItemId() + " забронирована");
        }
        bookingDtoRequest.setStatus(Status.WAITING);
        return BookingMapper.toBookingDtoResponse(
                bookingRepository.save(
                        BookingMapper.toBooking(bookingDtoRequest, user, item)),
                UserMapper.toUserDto(user),
                ItemMapper.toItemDto(item));
    }

    @Override
    public BookingDtoResponse updateBooking(Long userId, Long bookingId, Boolean approved) {
        log.info("Подтверждение бронирования");
        Booking booking = getBookingWithCheck(bookingId);
        checkBookingUser(userId, booking.getItem().getOwner().getId());
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new ValidationException("Статус у бронирования должен быть \"В ожидании подтверждения\"");
        }
        userService.getUserWithCheck(userId);
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.toBookingDtoResponse(
                bookingRepository.save(booking),
                UserMapper.toUserDto(booking.getBooker()),
                ItemMapper.toItemDto(booking.getItem()));
    }

    private Booking getBookingWithCheck(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с ID = " + bookingId + " не найдено"));
    }

    private void checkBookingUser(Long userId, Long ownerItemId) {
        if (!Objects.equals(userId, ownerItemId)) {
            throw new NotAccessException("У данного пользователя нет прав на данное действие");
        }
    }

    private void checkBookingUserForGet(Long userId, Long bookerId, Long ownerItemId) {
        if (!Objects.equals(userId, bookerId) && !Objects.equals(userId, ownerItemId)) {
            throw new NotAccessException("У данного пользователя нет прав на данное действие");
        }
    }
}
