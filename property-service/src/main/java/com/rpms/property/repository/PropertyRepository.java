package com.rpms.property.repository;

import com.rpms.property.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PropertyRepository extends JpaRepository<Property, UUID> {

    Page<Property> findAllByLandlordId(UUID landlordId, Pageable pageable);

    Page<Property> findAllByCityContainingIgnoreCaseAndIsActiveTrue(String city, Pageable pageable);

    Page<Property> findAllByIsActiveTrue(Pageable pageable);

    
    @Query("SELECT p FROM Property p WHERE p.isActive = true " +
           "AND (:city IS NULL OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
           "AND (:minPrice IS NULL OR p.pricePerNight >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.pricePerNight <= :maxPrice) " +
           "AND (:guests IS NULL OR p.maxGuests >= :guests)")
    Page<Property> searchProperties(@Param("city") String city,
                                    @Param("minPrice") BigDecimal minPrice,
                                    @Param("maxPrice") BigDecimal maxPrice,
                                    @Param("guests") Integer guests,
                                    Pageable pageable);

    List<Property> findAllByLandlordIdAndIsActiveTrue(UUID landlordId);
}