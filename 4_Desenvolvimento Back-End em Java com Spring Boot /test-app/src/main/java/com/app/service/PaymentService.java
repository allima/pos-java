package com.app.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.PaymentEvent;
import com.app.dto.PaymentRequest;
import com.app.dto.PaymentResponse;
import com.app.exception.PaymentException;
import com.app.model.Payment;
import com.app.model.PaymentMethod;
import com.app.model.PaymentStatus;
import com.app.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final PubSubService pubSubService;
	private final StorageService storageService;

	@Transactional
	public PaymentResponse createPayment(PaymentRequest request) {
		log.info("Criando pagamento: idempotencyKey={}", request.getIdempotencyKey());

		Optional<Payment> existingPayment = paymentRepository.findByIdempotencyKey(request.getIdempotencyKey());

		if (existingPayment.isPresent()) {
			log.info("Pagamento já existe (idempotência): {}", existingPayment.get().getId());
			return mapToResponse(existingPayment.get());
		}

		Payment payment = Payment.builder().idempotencyKey(request.getIdempotencyKey())
				.pagadorId(request.getPagadorId()).valor(request.getValor()).moeda(request.getMoeda())
				.method(request.getMetodo()).descricao(request.getDescricao()).status(PaymentStatus.CRIADO).build();

		payment = paymentRepository.save(payment);
		log.info("Pagamento criado com sucesso: id={}", payment.getId());

		PaymentEvent event = PaymentEvent.builder().paymentId(payment.getId()).pagadorId(payment.getPagadorId())
				.valor(payment.getValor()).moeda(payment.getMoeda()).method(payment.getMethod())
				.idempotencyKey(payment.getIdempotencyKey()).build();

		pubSubService.publishPaymentCreated(event);

		if (request.getAnexos() != null && !request.getAnexos().isEmpty()) {
			String path = storageService.uploadAttachment(request.getAnexos(), payment.getId().toString(),
					"application/octet-stream");
			payment.setAttachmentPath(path);
			paymentRepository.save(payment);
		}

		return mapToResponse(payment);
	}

	@Transactional(readOnly = true)
	public PaymentResponse getPaymentById(UUID id) {
		Payment payment = paymentRepository.findById(id)
				.orElseThrow(() -> new PaymentException("Pagamento não encontrado", 404));
		return mapToResponse(payment);
	}

	@Transactional(readOnly = true)
	public Page<PaymentResponse> getPayments(PaymentStatus status, PaymentMethod method, LocalDateTime startDate,
			LocalDateTime endDate, Pageable pageable) {
		Page<Payment> payments = paymentRepository.findFiltered(status, method, startDate, endDate, pageable);
		return payments.map(this::mapToResponse);
	}

	private PaymentResponse mapToResponse(Payment payment) {
		return PaymentResponse.builder().paymentId(payment.getId()).idempotencyKey(payment.getIdempotencyKey())
				.pagadorId(payment.getPagadorId()).valor(payment.getValor()).moeda(payment.getMoeda())
				.metodo(payment.getMethod()).descricao(payment.getDescricao()).status(payment.getStatus())
				.createdAt(payment.getCreatedAt()).build();
	}
}