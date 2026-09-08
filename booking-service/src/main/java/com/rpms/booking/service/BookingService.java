package com.rpms.booking.service;

import com.rpms.booking.dto.BookingResponseDto;
import com.rpms.booking.dto.CreateBookingRequest;
import com.rpms.booking.entity.Booking;
import com.rpms.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;

    // 🔥 TEMPORARY: Remove PropertyService dependency for now.
    // We will add Feign Client later.

    @Transactional
    @Retryable(include = OptimisticLockingFailureException.class, maxAttempts = 3)
    public BookingResponseDto createBooking(
            UUID tenantId,
            CreateBookingRequest request
    ) {

        // 1. Validate booking dates
        if (!request.getCheckOut().isAfter(request.getCheckIn())) {
            throw new IllegalArgumentException(
                    "Check-out date must be after check-in date"
            );
        }

        // 2. Check property availability
        List<Booking> overlappingBookings =
                bookingRepository.findOverlappingBookings(
                        request.getPropertyId(),
                        request.getCheckIn(),
                        request.getCheckOut()
                );

        if (!overlappingBookings.isEmpty()) {
            throw new RuntimeException(
                    "Property is already booked for the selected dates"
            );
        }

        // 3. 🔥 TEMPORARY: Hardcode the price instead of calling Property Service
        BigDecimal nightlyRate = BigDecimal.valueOf(150.00); // Replace with real call later

        // 4. Calculate number of nights
        long nights = ChronoUnit.DAYS.between(
                request.getCheckIn(),
                request.getCheckOut()
        );

        // 5. Calculate total price
        BigDecimal totalPrice =
                nightlyRate.multiply(BigDecimal.valueOf(nights));

        // 6. Create booking
        Booking booking = Booking.builder()
                .propertyId(request.getPropertyId())
                .tenantId(tenantId)
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .numberOfGuests(request.getNumberOfGuests())
                .totalPrice(totalPrice)
                .status(Booking.Status.PENDING)
                .build();

        // 7. Save booking
        Booking savedBooking = bookingRepository.save(booking);

        log.info(
                "Booking created with ID: {} for property: {}",
                savedBooking.getId(),
                savedBooking.getPropertyId()
        );

        // 8. Convert entity to response DTO
        return mapToResponse(savedBooking);
    }

    @Transactional(readOnly = true)
    public BookingResponseDto getBookingById(UUID bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found")
                );

        return mapToResponse(booking);
    }

    @Transactional(readOnly = true)
    public Page<BookingResponseDto> getBookingsForTenant(
            UUID tenantId,
            Pageable pageable
    ) {

        return bookingRepository
                .findAllByTenantId(tenantId, pageable)
                .map(this::mapToResponse);
    }

    @Transactional
    public void confirmBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != Booking.Status.PENDING) {
            throw new RuntimeException("Only PENDING bookings can be confirmed");
        }

        booking.setStatus(Booking.Status.CONFIRMED);
        bookingRepository.save(booking);
        log.info("Booking {} confirmed", bookingId);
    }

    private BookingResponseDto mapToResponse(Booking booking) {

        return BookingResponseDto.builder()
                .id(booking.getId())
                .propertyId(booking.getPropertyId())
                .tenantId(booking.getTenantId())
                .checkIn(booking.getCheckIn())
                .checkOut(booking.getCheckOut())
                .numberOfGuests(booking.getNumberOfGuests())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus().name())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}