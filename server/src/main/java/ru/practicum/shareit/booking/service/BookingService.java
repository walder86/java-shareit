package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.enumerated.State;

import java.util.List;

public interface BookingService {

    BookingDtoResponse getBookingById(Long userId, Long bookingId);

    List<BookingDtoResponse> getBookingByUser(Long bookerId, State state);

    List<BookingDtoResponse> getBookingByItemsUser(Long userOwnerItemId, State state);

    BookingDtoResponse createBooking(Long userId, BookingDtoRequest bookingDtoRequest);

    BookingDtoResponse updateBooking(Long userId, Long bookingId, Boolean approved);
}
