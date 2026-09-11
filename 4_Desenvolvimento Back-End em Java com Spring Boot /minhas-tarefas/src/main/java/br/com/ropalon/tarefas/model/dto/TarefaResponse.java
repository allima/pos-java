package br.com.ropalon.tarefas.model.dto;

public record TarefaResponse(Integer id,
		String descricao, 
		String status, 
		String dataEntrega,
		Integer categoriaId,
		Integer usuarioId) {

}
