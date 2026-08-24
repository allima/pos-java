package com.app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments", indexes = { @Index(name = "idx_payment_idempotency", columnList = "idempotency_key"),
		@Index(name = "idx_payment_status", columnList = "status"),
		@Index(name = "idx_payment_created_at", columnList = "created_at"),
		@Index(name = "idx_payment_method", columnList = "method") })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "idempotency_key", nullable = false, unique = true)
	private String idempotencyKey;

	@Column(name = "pagador_id", nullable = false)
	private UUID pagadorId;

	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal valor;

	@Column(nullable = false, length = 3)
	private String moeda;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentMethod method;

	@Column(length = 500)
	private String descricao;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStatus status;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "attachment_path")
	private String attachmentPath;

	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
		if (this.status == null) {
			this.status = PaymentStatus.CRIADO;
		}
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
