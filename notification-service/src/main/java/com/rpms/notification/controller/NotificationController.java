package com.rpms.notification.controller;

import com.rpms.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/booking-confirmation")
    public ResponseEntity<String> sendBookingConfirmation(
            @RequestParam UUID bookingId,
            @RequestParam UUID userId) {

        notificationService.sendBookingConfirmation(bookingId, userId);
        return ResponseEntity.ok("Notification sent successfully");
    }
}