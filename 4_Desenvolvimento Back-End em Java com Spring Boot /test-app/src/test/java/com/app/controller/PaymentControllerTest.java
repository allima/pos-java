package com.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.app.dto.PaymentRequest;
import com.app.dto.PaymentResponse;
import com.app.model.PaymentMethod;
import com.app.model.PaymentStatus;
import com.app.service.PaymentService;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    void shouldCreatePayment() {
        PaymentRequest request = PaymentRequest.builder()
            .idempotencyKey("key-1")
            .pagadorId(UUID.randomUUID())
            .valor(BigDecimal.valueOf(100))
            .moeda("BRL")
            .metodo(PaymentMethod.CARTAO)
            .descricao("descricao")
            .build();

        PaymentResponse expected = PaymentResponse.builder()
            .paymentId(UUID.randomUUID())
            .status(PaymentStatus.CRIADO)
            .build();

        when(paymentService.createPayment(request)).thenReturn(expected);

        ResponseEntity<PaymentResponse> response = paymentController.createPayment(request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(expected.getPaymentId(), response.getBody().getPaymentId());
        verify(paymentService).createPayment(request);
    }

    @Test
    void shouldGetPaymentById() {
        UUID paymentId = UUID.randomUUID();
        PaymentResponse expected = PaymentResponse.builder()
            .paymentId(paymentId)
            .status(PaymentStatus.CONFIRMADO)
            .build();

        when(paymentService.getPaymentById(paymentId)).thenReturn(expected);

        ResponseEntity<PaymentResponse> response = paymentController.getPayment(paymentId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(paymentId, response.getBody().getPaymentId());
        verify(paymentService).getPaymentById(paymentId);
    }

    @Test
    void shouldReturnFilteredPaymentsPage() {
        PaymentResponse item = PaymentResponse.builder()
            .paymentId(UUID.randomUUID())
            .status(PaymentStatus.CRIADO)
            .build();
        Pageable pageable = PageRequest.of(0, 20);
        Page<PaymentResponse> expectedPage = new PageImpl<>(List.of(item), pageable, 1);

        when(paymentService.getPayments(eq(PaymentStatus.CRIADO), eq(PaymentMethod.CONTA), any(LocalDateTime.class),
            any(LocalDateTime.class), eq(pageable))).thenReturn(expectedPage);

        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();

        ResponseEntity<Page<PaymentResponse>> response =
            paymentController.getPayments(PaymentStatus.CRIADO, PaymentMethod.CONTA, start, end, pageable);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().getTotalElements());
        verify(paymentService).getPayments(PaymentStatus.CRIADO, PaymentMethod.CONTA, start, end, pageable);
    }
}
