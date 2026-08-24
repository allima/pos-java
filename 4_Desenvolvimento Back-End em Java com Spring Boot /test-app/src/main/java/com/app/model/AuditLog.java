package com.app.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "audit_logs", indexes = { @Index(name = "idx_audit_timestamp", columnList = "timestamp"),
		@Index(name = "idx_audit_user", columnList = "usuario"),
		@Index(name = "idx_audit_resource", columnList = "recurso") })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String usuario;

	@Column(nullable = false)
	private String acao;

	@Column(nullable = false)
	private String recurso;

	@Column(nullable = false)
	private LocalDateTime timestamp;

	@Column(columnDefinition = "TEXT")
	private String resultado;

	@Column
	private Long latenciaMs;

	@Column(columnDefinition = "TEXT")
	private String metadata;
}