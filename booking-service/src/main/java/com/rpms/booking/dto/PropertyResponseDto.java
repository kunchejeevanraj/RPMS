package com.rpms.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) // ✅ Prevents errors if JSON has extra fields
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponseDto {

    private UUID id;
    private String title;
    private String description;
    private String city;
    private String state;
    private String country;
    private BigDecimal pricePerNight;
    private Integer maxGuests;
    private Integer bedrooms;
    private BigDecimal bathrooms;
    private String[] amenities;
    private String[] images;
    private boolean isActive;
}