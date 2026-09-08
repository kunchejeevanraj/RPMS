package com.rpms.payment.controller;

import com.rpms.payment.dto.InitiatePaymentRequest;
import com.rpms.payment.dto.PaymentResponseDto;
import com.rpms.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> initiatePayment(@Valid @RequestBody InitiatePaymentRequest request) {
        PaymentResponseDto response = paymentService.initiatePayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentResponseDto> getPaymentByBooking(@PathVariable UUID bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentByBookingId(bookingId));
    }
}