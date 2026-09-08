package com.rpms.payment.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.rpms.payment.dto.BookingResponseDto;

@FeignClient(name = "BOOKING-SERVICE")
public interface BookingServiceClient {
	
	 @GetMapping("/api/v1/bookings/{id}")
	  BookingResponseDto getBookingById(@PathVariable("id") UUID id);


    @PatchMapping("/api/v1/bookings/{id}/confirm")
    void confirmBooking(@PathVariable("id") UUID id);
} 