package com.app.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.app.dto.PaymentEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PubSubService {

	private final PubSubTemplate pubSubTemplate;
	private final ObjectMapper objectMapper;
	private final PaymentProcessingService paymentProcessingService;

	@Value("${gcp.pubsub.topics.payment-created}")
	private String paymentCreatedTopic;

	@Value("${gcp.pubsub.subscriptions.payment-processor}")
	private String paymentProcessorSubscription;

	private final AtomicInteger retryCounter = new AtomicInteger(0);

	@PostConstruct
	public void subscribe() {
		pubSubTemplate.subscribe(paymentProcessorSubscription, message -> {
			try {
				String payload = message.getPubsubMessage().getData().toStringUtf8();
				PaymentEvent event = objectMapper.readValue(payload, PaymentEvent.class);
				log.info("Mensagem recebida: paymentId={}", event.getPaymentId());
				paymentProcessingService.processPayment(event);
				message.ack();
				retryCounter.set(0);
			} catch (Exception e) {
				log.error("Erro ao processar mensagem", e);
				handleFailedMessage(message);
			}
		});
		log.info("Subscriber iniciado para: {}", paymentProcessorSubscription);
	}

	public void publishPaymentCreated(PaymentEvent event) {
		try {
			String payload = objectMapper.writeValueAsString(event);
			pubSubTemplate.publish(paymentCreatedTopic, payload.getBytes());
			log.info("Evento payment.created publicado: paymentId={}", event.getPaymentId());
		} catch (JsonProcessingException e) {
			log.error("Erro ao serializar evento", e);
		}
	}

	private void handleFailedMessage(BasicAcknowledgeablePubsubMessage message) {
		int retries = retryCounter.incrementAndGet();
		if (retries >= 3) {
			log.error("Mensagem falhou 3 vezes, enviando para DLQ");
			message.nack();
			retryCounter.set(0);
		} else {
			log.warn("Tentativa {} de processamento", retries);
			message.nack();
		}
	}
}
