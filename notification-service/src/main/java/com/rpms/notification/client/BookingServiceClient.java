package com.rpms.notification.client;

import com.rpms.notification.dto.BookingResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "BOOKING-SERVICE")
public interface BookingServiceClient {

    @GetMapping("/api/v1/bookings/{id}")
    BookingResponseDto getBookingById(@PathVariable("id") UUID id);
}