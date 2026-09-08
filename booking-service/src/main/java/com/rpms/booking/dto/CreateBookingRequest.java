package com.rpms.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {

    @NotNull(message = "Property ID is required")
    private UUID propertyId;

    @NotNull(message = "Check-in date is required")
    @Future(message = "Check-in must be in the future")
    private LocalDate checkIn;

    @NotNull(message = "Check-out date is required")
    @Future(message = "Check-out must be in the future")
    private LocalDate checkOut;

    @NotNull(message = "Number of guests is required")
    @Min(value = 1, message = "Must have at least 1 guest")
    private Integer numberOfGuests;
}