package com.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.PaymentEvent;
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
public class PaymentProcessingService {

	private final PaymentRepository paymentRepository;

	@Transactional
	public void processPayment(PaymentEvent event) {
		log.info("Processando pagamento: paymentId={}", event.getPaymentId());

		Payment payment = paymentRepository.findById(event.getPaymentId())
				.orElseThrow(() -> new PaymentException("Pagamento não encontrado", 404));

		payment.setStatus(PaymentStatus.PROCESSANDO);
		paymentRepository.save(payment);

		boolean approved = simulateProcessing(payment);

		if (approved) {
			payment.setStatus(PaymentStatus.CONFIRMADO);
			log.info("Pagamento confirmado: id={}", payment.getId());
		} else {
			payment.setStatus(PaymentStatus.RECUSADO);
			log.warn("Pagamento recusado: id={}", payment.getId());
		}

		paymentRepository.save(payment);
	}

	private boolean simulateProcessing(Payment payment) {
		if (payment.getMethod() == PaymentMethod.CARTAO) {
			return simulateAntifraud(payment);
		}
		return simulateBalanceCheck(payment);
	}

	private boolean simulateAntifraud(Payment payment) {
		double threshold = 10000.0;
		return payment.getValor().doubleValue() < threshold;
	}

	private boolean simulateBalanceCheck(Payment payment) {
		double maxBalance = 50000.0;
		return payment.getValor().doubleValue() <= maxBalance;
	}
}
