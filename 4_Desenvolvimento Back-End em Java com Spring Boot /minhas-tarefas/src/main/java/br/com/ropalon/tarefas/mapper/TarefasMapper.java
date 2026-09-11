package br.com.ropalon.tarefas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.ropalon.tarefas.model.Tarefa;
import br.com.ropalon.tarefas.model.dto.TarefaRequest;
import br.com.ropalon.tarefas.model.dto.TarefaResponse;

@Mapper(componentModel = "spring") 
public interface TarefasMapper {
	

	@Mapping(source = "categoria.id", target = "categoriaId")
	@Mapping(source = "usuario.id", target = "usuarioId")
	TarefaResponse toTarefaResponse(Tarefa tarefa);

	List<TarefaResponse> toTarefasResponseList(List<Tarefa> tarefas);

	@Mapping(source = "categoriaId", target = "categoria.id")
	@Mapping(source = "usuarioId", target = "usuario.id")
	@Mapping(target = "visivel", constant = "false")
	 @Mapping(target = "status", ignore = true)
	Tarefa toTarefa(TarefaRequest tarefaRequest);

}