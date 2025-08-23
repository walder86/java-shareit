package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.enumerated.Status;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoRequest {

    @FutureOrPresent(message = "Дата начала бронирования не может быть раньше текущего времени")
    @NotNull(message = "Дата начала бронирования не может быть пустой")
    private LocalDateTime start;
    @Future(message = "Дата окончания бронирования не может быть раньше текущего времени")
    @NotNull(message = "Дата окончания бронирования не может быть пустой")
    private LocalDateTime end;
    @NotNull(message = "ID вещи не может быть пустым")
    private Long itemId;
    private Status status;
}
