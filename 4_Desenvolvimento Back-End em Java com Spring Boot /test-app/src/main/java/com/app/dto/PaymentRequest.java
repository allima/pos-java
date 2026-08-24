package com.app.dto;

import java.math.BigDecimal;

import com.app.model.PaymentMethod;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

	@NotBlank(message = "Idempotency key é obrigatório")
	private String idempotencyKey;

	@NotNull(message = "Pagador ID é obrigatório")
	private java.util.UUID pagadorId;

	@NotNull(message = "Valor é obrigatório")
	@DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
	private BigDecimal valor;

	@NotBlank(message = "Moeda é obrigatória")
	@Pattern(regexp = "^[A-Z]{3}$", message = "Moeda deve ser ISO-4217 (3 letras maiúsculas)")
	private String moeda;

	@NotNull(message = "Método de pagamento é obrigatório")
	private PaymentMethod metodo;

	@Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
	private String descricao;

	
	private String anexos;
}