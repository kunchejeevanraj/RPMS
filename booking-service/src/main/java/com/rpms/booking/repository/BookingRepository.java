package com.rpms.booking.repository;

import com.rpms.booking.entity.Booking;
import com.rpms.booking.entity.Booking.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    // The magic query: Finds any booking that overlaps with the requested dates
    // This prevents double-booking!
    @Query("SELECT b FROM Booking b WHERE b.propertyId = :propertyId " +
           "AND b.status IN ('PENDING', 'CONFIRMED') " +
           "AND (:checkIn < b.checkOut AND :checkOut > b.checkIn)")
    List<Booking> findOverlappingBookings(@Param("propertyId") UUID propertyId,
                                          @Param("checkIn") LocalDate checkIn,
                                          @Param("checkOut") LocalDate checkOut);

    Page<Booking> findAllByTenantId(UUID tenantId, Pageable pageable);

    Page<Booking> findAllByPropertyId(UUID propertyId, Pageable pageable);

    List<Booking> findAllByStatusAndCheckInBefore(Status status, LocalDate date);
}