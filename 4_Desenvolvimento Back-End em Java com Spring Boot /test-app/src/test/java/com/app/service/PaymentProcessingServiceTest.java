package com.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.dto.PaymentEvent;
import com.app.exception.PaymentException;
import com.app.model.Payment;
import com.app.model.PaymentMethod;
import com.app.model.PaymentStatus;
import com.app.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
class PaymentProcessingServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentProcessingService paymentProcessingService;

    @Test
    void shouldConfirmCardPaymentBelowAntifraudThreshold() {
        PaymentEvent event = PaymentEvent.builder()
            .paymentId(UUID.randomUUID())
            .build();

        Payment payment = Payment.builder()
            .id(event.getPaymentId())
            .valor(BigDecimal.valueOf(5000.00))
            .method(PaymentMethod.CARTAO)
            .status(PaymentStatus.CRIADO)
            .build();

        when(paymentRepository.findById(event.getPaymentId())).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArgument(0));

        paymentProcessingService.processPayment(event);

        assertEquals(PaymentStatus.CONFIRMADO, payment.getStatus());
        verify(paymentRepository, times(2)).save(any(Payment.class));
    }

    @Test
    void shouldRejectContaPaymentAboveBalanceLimit() {
        PaymentEvent event = PaymentEvent.builder()
            .paymentId(UUID.randomUUID())
            .build();

        Payment payment = Payment.builder()
            .id(event.getPaymentId())
            .valor(BigDecimal.valueOf(70000.00))
            .method(PaymentMethod.CONTA)
            .status(PaymentStatus.CRIADO)
            .build();

        when(paymentRepository.findById(event.getPaymentId())).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArgument(0));

        paymentProcessingService.processPayment(event);

        assertEquals(PaymentStatus.RECUSADO, payment.getStatus());
        verify(paymentRepository, times(2)).save(any(Payment.class));
    }

    @Test
    void shouldThrowWhenPaymentDoesNotExist() {
        UUID paymentId = UUID.randomUUID();
        PaymentEvent event = PaymentEvent.builder().paymentId(paymentId).build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThrows(PaymentException.class, () -> paymentProcessingService.processPayment(event));
    }
}
