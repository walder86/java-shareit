package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.StateException;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String url, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(url + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(Long bookerId, BookingDtoRequest bookingDto) {
        checkDates(bookingDto.getStart(), bookingDto.getEnd());
        return post("", bookerId, bookingDto);
    }

    public ResponseEntity<Object> approveBooking(Long ownerId, String approved, Long bookingId) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        return patch("/" + bookingId + "?approved={approved}", ownerId, parameters, null);
    }

    public ResponseEntity<Object> getBookingById(Long bookingId, Long userId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookingByUser(Long userId, String state) {
        validateState(state);
        Map<String, Object> parameters = Map.of(
                "state", state
        );
        return get("?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingByItemsUser(Long userId, String state) {
        validateState(state);
        Map<String, Object> parameters = Map.of(
                "state", state
        );
        return get("/owner?state={state}", userId, parameters);
    }

    private void validateState(String state) {
        if (BookingState.from(state).isEmpty()) {
            throw new StateException("Неизвестное состояние: " + state);
        }
    }

    private void checkDates(LocalDateTime start, LocalDateTime end) {
        if (start.isEqual(end)) {
            throw new ValidationException("Дата начала и дата окончания бронирования не могут быть равны");
        }
        if (start.isAfter(end)) {
            throw new ValidationException("Дата начала бронирования не может быть позже даты окончания бронирования");
        }
    }
}