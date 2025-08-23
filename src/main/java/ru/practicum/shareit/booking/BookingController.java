package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.enumerated.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final String userIdHeader = "X-Sharer-User-Id";

    @PostMapping
    public BookingDtoResponse createBooking(
            @RequestHeader(userIdHeader) Long bookerId,
            @Valid @RequestBody BookingDtoRequest bookingDtoRequest) {
        return bookingService.createBooking(bookerId, bookingDtoRequest);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse updateBooking(
            @RequestHeader(userIdHeader) Long bookerId,
            @RequestParam Boolean approved,
            @PathVariable Long bookingId) {
        return bookingService.updateBooking(bookerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse getBookingById(
            @RequestHeader(userIdHeader) Long bookerId,
            @PathVariable Long bookingId) {
        return bookingService.getBookingById(bookerId, bookingId);
    }

    @GetMapping
    public List<BookingDtoResponse> getBookingByUser(
            @RequestHeader(userIdHeader) Long bookerId,
            @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getBookingByUser(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getBookingByItemsUser(
            @RequestHeader(userIdHeader) Long userOwnerItemId,
            @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getBookingByItemsUser(userOwnerItemId, state);
    }

}
