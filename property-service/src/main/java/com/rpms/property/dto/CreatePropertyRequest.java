package com.rpms.property.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePropertyRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    private String zipCode;

    @NotNull(message = "Price per night is required")
    @Min(value = 1, message = "Price must be at least 1")
    private BigDecimal pricePerNight;

    private BigDecimal cleaningFee;

    @NotNull(message = "Max guests is required")
    @Min(value = 1, message = "Must allow at least 1 guest")
    private Integer maxGuests;

    @NotNull(message = "Bedrooms is required")
    @Min(value = 1, message = "Must have at least 1 bedroom")
    private Integer bedrooms;

    @NotNull(message = "Bathrooms is required")
    @Min(value = 1, message = "Must have at least 1 bathroom")
    private BigDecimal bathrooms;

    private String[] amenities;
}