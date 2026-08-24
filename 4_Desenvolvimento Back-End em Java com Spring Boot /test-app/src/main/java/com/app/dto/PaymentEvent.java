package com.app.dto;

import com.app.model.PaymentMethod;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEvent {

    private UUID paymentId;
    private UUID pagadorId;
    private BigDecimal valor;
    private String moeda;
    private PaymentMethod method;
    private String idempotencyKey;
}
