package br.com.ropalon.tarefas.controller.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import br.com.ropalon.tarefas.controller.TarefaCategoriaController;
import br.com.ropalon.tarefas.controller.TarefaController;
import br.com.ropalon.tarefas.controller.UsuarioController;
import br.com.ropalon.tarefas.mapper.TarefasMapper;
import br.com.ropalon.tarefas.model.Tarefa;
import br.com.ropalon.tarefas.model.dto.TarefaResponse;

@Component
public class TarefaModelAssembler implements RepresentationModelAssembler<Tarefa, EntityModel<TarefaResponse>> {

	private final TarefasMapper mapper;

	public TarefaModelAssembler(TarefasMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public EntityModel<TarefaResponse> toModel(Tarefa tarefa) {

		var tarefaref = mapper.toTarefaResponse(tarefa);

		EntityModel<TarefaResponse> tarefaModel = EntityModel.of(tarefaref,
				linkTo(methodOn(TarefaController.class).umaTarefa(tarefaref.id())).withSelfRel(),
				linkTo(methodOn(TarefaController.class).todasTarefas(new HashMap<>())).withRel("tarefas"),
				linkTo(methodOn(TarefaCategoriaController.class).umaCategoria(tarefaref.categoriaId()))
						.withRel("categorias"),
				linkTo(methodOn(UsuarioController.class).umUsuario(tarefaref.usuarioId())).withRel("usuarios"));
		return tarefaModel;
	}

}
