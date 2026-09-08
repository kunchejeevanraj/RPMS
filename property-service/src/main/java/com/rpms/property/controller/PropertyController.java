package com.rpms.property.controller;

import com.rpms.property.dto.CreatePropertyRequest;
import com.rpms.property.dto.PropertyResponseDto;
import com.rpms.property.service.PropertyService;
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
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    // 1. Create Property (Landlord only)
    @PostMapping
    public ResponseEntity<PropertyResponseDto> createProperty( 
            @RequestHeader("X-User-Id") UUID landlordId, // Temporary - we will replace with JWT later
            @Valid @RequestBody CreatePropertyRequest request) {
        
        PropertyResponseDto response = propertyService.createProperty(landlordId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. Get Property by ID
    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponseDto> getProperty(@PathVariable UUID id) {
        return ResponseEntity.ok(propertyService.getPropertyById(id));
    }

    // 3. Search Properties by City
    @GetMapping("/search")
    public ResponseEntity<Page<PropertyResponseDto>> searchProperties(
            @RequestParam String city,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(propertyService.searchProperties(city, pageable));
    }

    // 4. Get All Active Properties
    @GetMapping
    public ResponseEntity<Page<PropertyResponseDto>> getAllProperties(
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(propertyService.getAllActiveProperties(pageable));
    }
}