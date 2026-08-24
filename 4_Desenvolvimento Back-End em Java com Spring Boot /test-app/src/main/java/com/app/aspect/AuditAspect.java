package com.app.aspect;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.app.model.AuditLog;
import com.app.repository.AuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditAspect {

	private final AuditLogRepository auditLogRepository;
	private final ObjectMapper objectMapper;

	@Around("within(com.app.controller..*) || within(com.app.service..*)")
	public Object auditMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		String usuario = getCurrentUser();
		String acao = joinPoint.getSignature().getName();
		String recurso = joinPoint.getSignature().getDeclaringTypeName() + "." + acao;
		LocalDateTime timestamp = LocalDateTime.now();

		long startTime = System.currentTimeMillis();
		Object result;
		String resultado = null;

		try {
			result = joinPoint.proceed();
			resultado = "SUCCESS";
			return result;
		} catch (Exception e) {
			resultado = "ERROR: " + e.getMessage();
			throw e;
		} finally {
			long endTime = System.currentTimeMillis();
			long latenciaMs = endTime - startTime;

			AuditLog auditLog = AuditLog.builder().usuario(usuario).acao(acao).recurso(recurso).timestamp(timestamp)
					.resultado(resultado).latenciaMs(latenciaMs).metadata(buildMetadata(joinPoint.getArgs())).build();

			try {
				auditLogRepository.save(auditLog);
			} catch (Exception e) {
				log.warn("Falha ao salvar audit log", e);
			}

			log.info("AUDIT: usuario={}, acao={}, recurso={}, resultado={}, latenciaMs={}", usuario, acao, recurso,
					resultado, latenciaMs);
		}
	}

	private String getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			return authentication.getName();
		}
		return "anonymous";
	}

	private String buildMetadata(Object[] args) {
		try {
			Map<String, Object> params = new HashMap<>();
			for (int i = 0; i < args.length; i++) {
				params.put("arg" + i, args[i]);
			}
			return objectMapper.writeValueAsString(params);
		} catch (Exception e) {
			return "{}";
		}
	}
}