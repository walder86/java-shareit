package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.enumerated.Status;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoRequest {

    @NotNull(message = "Дата начала бронирования не может быть пустой")
    private LocalDateTime start;
    @NotNull(message = "Дата окончания бронирования не может быть пустой")
    private LocalDateTime end;
    @NotNull(message = "ID вещи не может быть пустым")
    private Long itemId;
    private Status status;
}
