package br.com.ropalon.tarefas.controller.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import br.com.ropalon.tarefas.controller.UsuarioController;
import br.com.ropalon.tarefas.mapper.UsuarioMapper;
import br.com.ropalon.tarefas.model.Usuario;
import br.com.ropalon.tarefas.model.dto.UsuarioResponse;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<UsuarioResponse>> {

	private final UsuarioMapper mapper;

	public UsuarioModelAssembler(UsuarioMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public EntityModel<UsuarioResponse> toModel(Usuario usuario) {
		var usuarioResponse = mapper.toUsuarioResponse(usuario);

		return EntityModel.of(usuarioResponse,
				linkTo(methodOn(UsuarioController.class).umUsuario(usuarioResponse.id())).withSelfRel(),
				linkTo(methodOn(UsuarioController.class).todosUsuarios()).withRel("usuarios"));
	}
}
