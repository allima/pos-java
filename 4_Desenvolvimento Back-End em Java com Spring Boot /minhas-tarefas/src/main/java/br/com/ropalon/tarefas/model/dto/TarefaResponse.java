package br.com.ropalon.tarefas.model.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TarefaResponse(Integer id,
		@NotBlank(message = "{tarefa.descricao.not_blank}")
		@Size(min = 5, max = 150, message = "{tarefa.descricao.size}")
		String descricao, 
		String status, 
		@FutureOrPresent(message = "{tarefa.descricao.future-or-present}")
		LocalDate dataEntrega,
		@NotBlank(message = "{tarefa.categoria.not_blank}")
		@Min(value = 1, message = "{tarefa.categoria.min}")
		Integer categoriaId,
		@NotBlank(message = "{tarefa.usuario.not_blank}")
		@Min(value = 1, message = "{tarefa.usuario.min}")
		Integer usuarioId) {

}
