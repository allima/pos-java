package com.app.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.PaymentRequest;
import com.app.dto.PaymentResponse;
import com.app.model.PaymentMethod;
import com.app.model.PaymentStatus;
import com.app.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping
	@PreAuthorize("hasAnyRole('SERVICE', 'ADMIN')")
	public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
		PaymentResponse response = paymentService.createPayment(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
	public ResponseEntity<PaymentResponse> getPayment(@PathVariable UUID id) {
		PaymentResponse response = paymentService.getPaymentById(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
	public ResponseEntity<Page<PaymentResponse>> getPayments(@RequestParam(required = false) PaymentStatus status,
			@RequestParam(required = false) PaymentMethod method,
			@RequestParam(required = false) LocalDateTime startDate,
			@RequestParam(required = false) LocalDateTime endDate, @PageableDefault(size = 20) Pageable pageable) {
		Page<PaymentResponse> payments = paymentService.getPayments(status, method, startDate, endDate, pageable);
		return ResponseEntity.ok(payments);
	}
}
