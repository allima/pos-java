package com.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.app.model.PaymentMethod;
import com.app.model.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

	private UUID paymentId;
	private String idempotencyKey;
	private UUID pagadorId;
	private BigDecimal valor;
	private String moeda;
	private PaymentMethod metodo;
	private String descricao;
	private PaymentStatus status;
	private LocalDateTime createdAt;
}
