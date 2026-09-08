package com.rpms.property.service;

import com.rpms.property.dto.CreatePropertyRequest;
import com.rpms.property.dto.PropertyResponseDto;
import com.rpms.property.entity.Property;
import com.rpms.property.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyService {

    private final PropertyRepository propertyRepository;

    @Transactional
    public PropertyResponseDto createProperty(UUID landlordId, CreatePropertyRequest request) {
        Property property = Property.builder()
                .landlordId(landlordId)
                .title(request.getTitle())
                .description(request.getDescription())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .zipCode(request.getZipCode())
                .pricePerNight(request.getPricePerNight())
                .cleaningFee(request.getCleaningFee())
                .maxGuests(request.getMaxGuests())
                .bedrooms(request.getBedrooms())
                .bathrooms(request.getBathrooms())
                .amenities(request.getAmenities())
                .isActive(true)
                .build();

        Property saved = propertyRepository.save(property);
        log.info("Property created with ID: {} by landlord: {}", saved.getId(), landlordId);

        return mapToResponse(saved);
    }

    public PropertyResponseDto getPropertyById(UUID propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        return mapToResponse(property);
    }

    public Page<PropertyResponseDto> searchProperties(String city, Pageable pageable) {
        return propertyRepository.findAllByCityContainingIgnoreCaseAndIsActiveTrue(city, pageable)
                .map(this::mapToResponse);
    }

    public Page<PropertyResponseDto> getAllActiveProperties(Pageable pageable) {
        return propertyRepository.findAllByIsActiveTrue(pageable)
                .map(this::mapToResponse);
    }

    private PropertyResponseDto mapToResponse(Property property) {
        return PropertyResponseDto.builder()
                .id(property.getId())
                .landlordId(property.getLandlordId())
                .title(property.getTitle())
                .description(property.getDescription())
                .address(property.getAddress())
                .city(property.getCity())
                .state(property.getState())
                .country(property.getCountry())
                .zipCode(property.getZipCode())
                .pricePerNight(property.getPricePerNight())
                .cleaningFee(property.getCleaningFee())
                .maxGuests(property.getMaxGuests())
                .bedrooms(property.getBedrooms())
                .bathrooms(property.getBathrooms())
                .amenities(property.getAmenities())
                .images(property.getImages())
                .isActive(property.isActive())
                .averageRating(property.getAverageRating())
                .createdAt(property.getCreatedAt())
                .updatedAt(property.getUpdatedAt())
                .build();
    }
}