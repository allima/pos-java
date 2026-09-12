package br.com.ropalon.tarefas.controller;

import java.util.List;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.ropalon.tarefas.exception.TarefaStatusException;
import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		List<ErroResponse> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> new ErroResponse(error.getField(), error.getDefaultMessage())).toList();

		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErroResponse entityNotFoundHandler(EntityNotFoundException ex) {
		ErroResponse error = new ErroResponse("Recurso não encontrado: " + ex.getMessage());
		return error;
	}

	@ExceptionHandler(TarefaStatusException.class)
	public ResponseEntity<?> alterarStatusExceptionHandler(TarefaStatusException ex) {
		return ResponseEntity
				.status(HttpStatus.METHOD_NOT_ALLOWED)
				.header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
				.body(Problem.create().withTitle("Alteração de status não permitida")
						.withDetail("Voçê não pode realizar esta operação: " + ex.getMessage()));

	}

}
