package br.com.ropalon.tarefas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ropalon.tarefas.controller.assembler.UsuarioModelAssembler;
import br.com.ropalon.tarefas.mapper.UsuarioMapper;
import br.com.ropalon.tarefas.model.Usuario;
import br.com.ropalon.tarefas.model.dto.UsuarioRequest;
import br.com.ropalon.tarefas.model.dto.UsuarioResponse;
import br.com.ropalon.tarefas.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	private final UsuarioService service;
	private final UsuarioMapper mapper;
	private final UsuarioModelAssembler assembler;

	public UsuarioController(UsuarioService service, UsuarioMapper mapper, UsuarioModelAssembler assembler) {
		this.service = service;
		this.mapper = mapper;
		this.assembler = assembler;
	}

	@GetMapping
	public CollectionModel<EntityModel<UsuarioResponse>> todosUsuarios() {
		List<Usuario> usuarios = service.todosUsuarios();
		var usuarioModels = usuarios.stream().map(assembler::toModel).toList();

		return CollectionModel.of(usuarioModels,
				linkTo(methodOn(UsuarioController.class).todosUsuarios()).withSelfRel());
	}

	@GetMapping("/{id}")
	public EntityModel<UsuarioResponse> umUsuario(@PathVariable Integer id) {
		var usuario = service.buscarUsuarioPorId(id);
		return assembler.toModel(usuario);
	}

	@PostMapping
	public UsuarioResponse salvarUsuario(@Valid @RequestBody UsuarioRequest request) {
		var usuario = mapper.toUsuario(request);
		return mapper.toUsuarioResponse(service.salvarUsuario(usuario));
	}

	@PutMapping("/{id}")
	public UsuarioResponse atualizarUsuario(@PathVariable Integer id, @Valid @RequestBody UsuarioRequest request) {
		var usuarioAtualizado = mapper.toUsuario(request);
		return mapper.toUsuarioResponse(service.atualizarUsuario(id, usuarioAtualizado));
	}

	@DeleteMapping("/{id}")
	public void deletarUsuario(@PathVariable Integer id) {
		service.deletarUsuario(id);
	}
}
