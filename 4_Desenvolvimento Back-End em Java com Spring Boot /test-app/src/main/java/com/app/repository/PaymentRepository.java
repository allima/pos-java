package com.app.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.model.Payment;
import com.app.model.PaymentMethod;
import com.app.model.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
    
    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);
    
    Page<Payment> findByMethod(PaymentMethod method, Pageable pageable);
    
    @Query("SELECT p FROM Payment p WHERE " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:method IS NULL OR p.method = :method) AND " +
           "(:startDate IS NULL OR p.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR p.createdAt <= :endDate)")
    Page<Payment> findFiltered(
        @Param("status") PaymentStatus status,
        @Param("method") PaymentMethod method,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );
    
    @Query(value = """
        SELECT 
            method,
            status,
            COUNT(*) as count,
            SUM(valor) as total,
            AVG(valor) as avg_valor
        FROM payments
        WHERE created_at >= CURRENT_TIMESTAMP - INTERVAL '7' DAY
        GROUP BY method, status
        ORDER BY method, status
        """, nativeQuery = true)
    List<Object[]> findReconciliationAggregated();
}