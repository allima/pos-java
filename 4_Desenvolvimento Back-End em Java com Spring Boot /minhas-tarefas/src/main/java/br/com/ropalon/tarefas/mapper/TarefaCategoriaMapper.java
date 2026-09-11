package br.com.ropalon.tarefas.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import br.com.ropalon.tarefas.model.TarefaCategoria;
import br.com.ropalon.tarefas.model.dto.TarefaCategoriaRequest;
import br.com.ropalon.tarefas.model.dto.TarefaCategoriaResponse;

@Mapper(componentModel = "spring")
public interface TarefaCategoriaMapper {

	TarefaCategoriaResponse toTarefaCategoriaResponse(TarefaCategoria categoria);

	List<TarefaCategoriaResponse> toTarefaCategoriaResponseList(List<TarefaCategoria> categorias);

	TarefaCategoria toTarefaCategoria(TarefaCategoriaRequest request);
}
