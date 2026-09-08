package com.rpms.notification.service;

import com.rpms.notification.client.BookingServiceClient;
import com.rpms.notification.dto.BookingResponseDto;
import com.rpms.notification.entity.Notification;
import com.rpms.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final BookingServiceClient bookingServiceClient; // ✅ Feign Client, not local service

    public void sendBookingConfirmation(UUID bookingId, UUID userId) {
        System.out.println("Just entered");
    	// 1. Fetch booking details via Feign Client
        BookingResponseDto booking = bookingServiceClient.getBookingById(bookingId);

        // 2. Build email content
        String subject = "Booking Confirmed!";
        String body = String.format(
                "Dear User,\n\nYour booking for Property ID: %s is confirmed!\n" +
                "Check-in: %s\nCheck-out: %s\nTotal: $%.2f\n\nThank you!",
                booking.getPropertyId(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                booking.getTotalPrice()
        );

        // 3. Create notification record
        Notification notification = Notification.builder()
                .userId(userId)
                .type("EMAIL")
                .subject(subject)
                .body(body)
                .status(Notification.Status.SENT)
                .sentAt(LocalDateTime.now())
                .build();

        // 4. Save to database
        notificationRepository.save(notification);
        log.info("📧 Notification saved for booking: {}", bookingId);
    }
}