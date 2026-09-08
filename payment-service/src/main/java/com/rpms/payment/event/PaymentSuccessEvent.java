package com.rpms.payment.event; // Change package for booking/notification

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSuccessEvent {
    private UUID bookingId;
    private UUID paymentId;
    private BigDecimal amount;
}