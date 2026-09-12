package br.com.ropalon.tarefas.controller.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import br.com.ropalon.tarefas.controller.TarefaCategoriaController;
import br.com.ropalon.tarefas.mapper.TarefaCategoriaMapper;
import br.com.ropalon.tarefas.model.TarefaCategoria;
import br.com.ropalon.tarefas.model.dto.TarefaCategoriaResponse;

@Component
public class TarefaCategoriaModelAssembler
		implements RepresentationModelAssembler<TarefaCategoria, EntityModel<TarefaCategoriaResponse>> {

	private final TarefaCategoriaMapper mapper;

	public TarefaCategoriaModelAssembler(TarefaCategoriaMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public EntityModel<TarefaCategoriaResponse> toModel(TarefaCategoria categoria) {
		var categoriaResponse = mapper.toTarefaCategoriaResponse(categoria);

		var categoriaModel = EntityModel.of(categoriaResponse,
				linkTo(methodOn(TarefaCategoriaController.class).umaCategoria(categoriaResponse.id())).withSelfRel(),
				linkTo(methodOn(TarefaCategoriaController.class).todasCategorias()).withRel("categorias"));
			
		return categoriaModel;

	}
}
