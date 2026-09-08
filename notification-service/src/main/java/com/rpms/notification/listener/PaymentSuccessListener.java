package com.rpms.notification.listener;

import com.rpms.notification.entity.Notification;
import com.rpms.notification.event.PaymentSuccessEvent;
import com.rpms.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentSuccessListener {

    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "payment-success-topic", groupId = "notification-group")
    public void handlePaymentSuccess(PaymentSuccessEvent event) {
        log.info("📥 Notification Service received event for booking: {}", event.getBookingId());

        Notification notification = Notification.builder()
                .userId(UUID.randomUUID())
                .type("EMAIL")
                .subject("Booking Confirmed!")
                .body("Your booking " + event.getBookingId() + " is confirmed. Amount: $" + event.getAmount())
                .status(Notification.Status.SENT)
                .sentAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
        log.info("📧 Notification saved for booking: {}", event.getBookingId());
    }
}