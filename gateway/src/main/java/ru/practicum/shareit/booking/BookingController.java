package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

@RestController
@RequestMapping("/bookings")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookingController {

	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") Long bookerId,
												@Valid @RequestBody BookingDtoRequest bookingDto) {
		return bookingClient.createBooking(bookerId, bookingDto);
	}

	@PatchMapping("{bookingId}")
	public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") Long ownerId,
												 @RequestParam String approved,
												 @PathVariable Long bookingId) {
		return bookingClient.approveBooking(ownerId, approved, bookingId);
	}

	@GetMapping("{bookingId}")
	public ResponseEntity<Object> getBookingById(
			@PathVariable Long bookingId,
			@RequestHeader("X-Sharer-User-Id") Long userId) {
		return bookingClient.getBookingById(bookingId, userId);
	}

	@GetMapping
	public ResponseEntity<Object> getBookingByUser(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@RequestParam(defaultValue = "ALL") String state) {
		return bookingClient.getBookingByUser(userId, state);
	}

	@GetMapping("owner")
	public ResponseEntity<Object> getBookingByItemsUser(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@RequestParam(defaultValue = "ALL") String state) {
		return bookingClient.getBookingByItemsUser(userId, state);
	}
}