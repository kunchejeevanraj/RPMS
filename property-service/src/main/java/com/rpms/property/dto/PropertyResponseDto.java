package com.rpms.property.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponseDto {

    private UUID id;
    private UUID landlordId;
    private String title;
    private String description;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private BigDecimal pricePerNight;
    private BigDecimal cleaningFee;
    private Integer maxGuests;
    private Integer bedrooms;
    private BigDecimal bathrooms;
    private String[] amenities;
    private String[] images;
    private boolean isActive;
    private BigDecimal averageRating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}