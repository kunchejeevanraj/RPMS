package com.rpms.booking.listener;

import com.rpms.booking.entity.Booking;
import com.rpms.booking.event.PaymentSuccessEvent;
import com.rpms.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentSuccessListener {

    private final BookingRepository bookingRepository;

    @KafkaListener(topics = "payment-success-topic", groupId = "booking-group")
    @Transactional
    public void handlePaymentSuccess(PaymentSuccessEvent event) {
        log.info("📥 Booking Service received event for booking: {}", event.getBookingId());

        Booking booking = bookingRepository.findById(event.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() == Booking.Status.PENDING) {
            booking.setStatus(Booking.Status.CONFIRMED);
            bookingRepository.save(booking);
            log.info("✅ Booking {} confirmed via Kafka event!", event.getBookingId());
        }
    }
}