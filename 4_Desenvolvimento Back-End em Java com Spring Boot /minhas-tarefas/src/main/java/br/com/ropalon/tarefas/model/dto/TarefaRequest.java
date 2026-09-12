package br.com.ropalon.tarefas.model.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TarefaRequest(
		Integer id, 
		@NotBlank(message = "{tarefa.descricao.not_blank}")
		@Size(min = 5, max = 150, message = "{tarefa.descricao.size}")
		String descricao, 
		@FutureOrPresent(message = "{tarefa.dataEntrega.future-or-present}")
		LocalDate dataEntrega,
		Integer categoriaId,
		Integer usuarioId) {
}
