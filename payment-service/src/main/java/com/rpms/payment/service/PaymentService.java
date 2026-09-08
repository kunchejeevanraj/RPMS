package com.rpms.payment.service;

import com.rpms.payment.client.BookingServiceClient;
import com.rpms.payment.dto.BookingResponseDto;
import com.rpms.payment.dto.InitiatePaymentRequest;
import com.rpms.payment.dto.PaymentResponseDto;
import com.rpms.payment.entity.Payment;
import com.rpms.payment.event.PaymentSuccessEvent;
import com.rpms.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingServiceClient bookingServiceClient; // ✅ Feign Client
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public PaymentResponseDto initiatePayment(InitiatePaymentRequest request) {


        // 1. Check if payment already exists for this booking
        if (paymentRepository.existsByBookingId(request.getBookingId())) {
            throw new RuntimeException("Payment already initiated for this booking");
        }

        // 2. Fetch the booking via Feign Client
        BookingResponseDto booking = bookingServiceClient.getBookingById(request.getBookingId());

        // 3. Validate booking status (Must be PENDING)
        if (!"PENDING".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException("Only PENDING bookings can be paid for. Current status: " + booking.getStatus());
        }

        // 4. Simulate external payment gateway (Stripe/PayPal)
        String transactionId = "TXN_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        boolean paymentSuccess = true;

        // 5. Create Payment Entity
        Payment payment = Payment.builder()
                .bookingId(request.getBookingId())
                .amount(booking.getTotalPrice())
                .currency("USD")
                .paymentMethod(request.getPaymentMethod())
                .transactionId(transactionId)
                .status(paymentSuccess ? Payment.Status.SUCCESS : Payment.Status.FAILED)
                .errorMessage(paymentSuccess ? null : "Payment gateway declined the transaction")
                .build();

        Payment saved = paymentRepository.save(payment);
        log.info("Payment processed with ID: {} for booking: {}", saved.getId(), saved.getBookingId());

        // 6. If payment is SUCCESS, update the Booking status to CONFIRMED
        if (paymentSuccess) {
            // Publish Kafka event instead of calling Booking directly
            PaymentSuccessEvent event = PaymentSuccessEvent.builder()
                    .bookingId(request.getBookingId())
                    .paymentId(saved.getId())
                    .amount(saved.getAmount())
                    .build();
            
            kafkaTemplate.send("payment-success-topic", event);
            log.info("📤 Published PaymentSuccessEvent for booking: {}", request.getBookingId());
        }

        // 7. Return Response
        return mapToResponse(saved, paymentSuccess ? "Payment processed successfully" : "Payment failed. Please try again.");
    }

    public PaymentResponseDto getPaymentByBookingId(UUID bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for booking: " + bookingId));
        return mapToResponse(payment, null);
    }

    private PaymentResponseDto mapToResponse(Payment payment, String message) {
        return PaymentResponseDto.builder()
                .id(payment.getId())
                .bookingId(payment.getBookingId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod())
                .transactionId(payment.getTransactionId())
                .status(payment.getStatus().name())
                .message(message != null ? message : "Payment status: " + payment.getStatus().name())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}