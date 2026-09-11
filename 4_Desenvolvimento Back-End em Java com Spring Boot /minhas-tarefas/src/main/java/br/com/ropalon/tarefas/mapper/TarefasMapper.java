package br.com.ropalon.tarefas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.ropalon.tarefas.model.Tarefa;
import br.com.ropalon.tarefas.model.dto.TarefaResponse;

@Mapper(componentModel = "spring") 
public interface TarefasMapper {
	

	@Mapping(source = "categoria.id", target = "categoriaId")
	@Mapping(source = "usuario.id", target = "usuarioId")
	TarefaResponse toTarefaResponse(Tarefa tarefa);

	List<TarefaResponse> toTarefasResponseList(List<Tarefa> tarefas);

}