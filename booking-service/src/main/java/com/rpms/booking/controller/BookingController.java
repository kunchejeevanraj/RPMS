package com.rpms.booking.controller;

import com.rpms.booking.dto.BookingResponseDto;
import com.rpms.booking.dto.CreateBookingRequest;
import com.rpms.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(
            @RequestHeader("X-User-Id") UUID tenantId, // Temporary, JWT will replace this
            @Valid @RequestBody CreateBookingRequest request) {

        BookingResponseDto response = bookingService.createBooking(tenantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBooking(@PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<Page<BookingResponseDto>> getBookingsForTenant(
            @PathVariable UUID tenantId,
            @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(bookingService.getBookingsForTenant(tenantId, pageable));
    }
}