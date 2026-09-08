package com.rpms.booking.client;

import com.rpms.booking.dto.PropertyResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "PROPERTY-SERVICE")  // Calls the service registered in Eureka
public interface PropertyServiceClient {

    @GetMapping("/api/v1/properties/{id}")
    PropertyResponseDto getPropertyById(@PathVariable("id") UUID id);
}