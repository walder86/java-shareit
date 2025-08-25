package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    public static BookingDtoResponse toBookingDtoResponse(Booking booking, UserDto userDto, ItemDto itemDto) {
        return new BookingDtoResponse(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                itemDto,
                userDto,
                booking.getStatus()
        );
    }

    public static Booking toBooking(BookingDtoRequest bookingDtoRequest, User booker, Item item) {
        return new Booking(
                bookingDtoRequest.getStart(),
                bookingDtoRequest.getEnd(),
                item,
                booker,
                bookingDtoRequest.getStatus()
        );
    }
}
