package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.enumerated.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    Collection<Booking> findAllByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long bookerId, LocalDateTime startBefore, LocalDateTime endAfter);

    Collection<Booking> findAllByBookerIdAndEndIsBeforeOrderByStartDesc(long bookerId, LocalDateTime endAfter);

    Collection<Booking> findAllByBookerIdAndStartIsAfterOrderByStartDesc(long bookerId, LocalDateTime endAfter);

    Collection<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long bookerId, Status status);

    List<Booking> findAllByItemIdInOrderByStartDesc(Collection<Long> itemId);

    Collection<Booking> findAllByItemIdInAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Collection<Long> itemId, LocalDateTime startBefore, LocalDateTime endAfter);

    Collection<Booking> findAllByItemIdInAndEndIsBeforeOrderByStartDesc(Collection<Long> itemId, LocalDateTime endAfter);

    Collection<Booking> findAllByItemIdInAndStartIsAfterOrderByStartDesc(Collection<Long> itemId, LocalDateTime endAfter);

    Collection<Booking> findAllByItemIdInAndStatusOrderByStartDesc(Collection<Long> itemId, Status status);

    boolean existsBookingByItemIdAndBookerIdAndStatusAndEndIsBefore(Long itemId, long bookerId, Status status, LocalDateTime endBefore);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(Long itemId, LocalDateTime startBefore, Status status);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(Long itemId, LocalDateTime startAfter, Status status);
}
