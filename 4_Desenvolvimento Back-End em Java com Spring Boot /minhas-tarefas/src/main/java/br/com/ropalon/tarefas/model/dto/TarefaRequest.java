package br.com.ropalon.tarefas.model.dto;

import java.time.LocalDate;

public record TarefaRequest(
		Integer id, 
		String descricao, 
		LocalDate dataEntrega,
		Integer categoriaId,
		Integer usuarioId) {
}
