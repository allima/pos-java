package com.app.service;


import com.app.dto.PaymentEvent;
import com.app.dto.PaymentRequest;
import com.app.dto.PaymentResponse;
import com.app.model.Payment;
import com.app.model.PaymentMethod;
import com.app.model.PaymentStatus;
import com.app.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    
    @Mock
    private PaymentRepository paymentRepository;
    
    @Mock
    private PubSubService pubSubService;
    
    @Mock
    private StorageService storageService;
    
    @InjectMocks
    private PaymentService paymentService;
    
    private PaymentRequest paymentRequest;
    
    @BeforeEach
    void setUp() {
        paymentRequest = PaymentRequest.builder()
            .idempotencyKey("test-key-123")
            .pagadorId(UUID.randomUUID())
            .valor(BigDecimal.valueOf(100.00))
            .moeda("BRL")
            .metodo(PaymentMethod.CARTAO)
            .descricao("Test payment")
            .build();
    }
    
    @Test
    void shouldCreatePaymentSuccessfully() {
        when(paymentRepository.findByIdempotencyKey(anyString())).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> {
            Payment p = i.getArgument(0);
            p.setId(UUID.randomUUID());
            p.setPagadorId(paymentRequest.getPagadorId());
            p.setValor(paymentRequest.getValor());
            p.setMoeda(paymentRequest.getMoeda());
            p.setMethod(paymentRequest.getMetodo());
            p.setDescricao(paymentRequest.getDescricao());
            p.setCreatedAt(LocalDateTime.now());
            return p;
        });
        
        PaymentResponse response = paymentService.createPayment(paymentRequest);
        
        assertNotNull(response);
        assertNotNull(response.getPaymentId());
        assertEquals(PaymentStatus.CRIADO, response.getStatus());
        verify(pubSubService, times(1)).publishPaymentCreated(any(PaymentEvent.class));
    }

    @Test
    void shouldUploadAttachmentWhenPresent() {
        paymentRequest.setAnexos("payload-base64");

        when(paymentRepository.findByIdempotencyKey(anyString())).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> {
            Payment p = i.getArgument(0);
            if (p.getId() == null) {
                p.setId(UUID.randomUUID());
                p.setCreatedAt(LocalDateTime.now());
            }
            return p;
        });
        when(storageService.uploadAttachment(eq("payload-base64"), anyString(), eq("application/octet-stream")))
            .thenReturn("gs://bucket/file.pdf");

        PaymentResponse response = paymentService.createPayment(paymentRequest);

        assertNotNull(response);
        verify(storageService, times(1))
            .uploadAttachment(eq("payload-base64"), anyString(), eq("application/octet-stream"));
        verify(paymentRepository, times(2)).save(any(Payment.class));
    }
    
    @Test
    void shouldReturnExistingPaymentWhenIdempotent() {
        Payment existingPayment = Payment.builder()
            .id(UUID.randomUUID())
            .idempotencyKey(paymentRequest.getIdempotencyKey())
            .status(PaymentStatus.CONFIRMADO)
            .build();
        
        when(paymentRepository.findByIdempotencyKey(anyString()))
            .thenReturn(Optional.of(existingPayment));
        
        PaymentResponse response = paymentService.createPayment(paymentRequest);
        
        assertNotNull(response);
        assertEquals(PaymentStatus.CONFIRMADO, response.getStatus());
        verify(paymentRepository, never()).save(any(Payment.class));
    }
}