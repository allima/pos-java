package br.com.ropalon.tarefas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ropalon.tarefas.mapper.TarefaCategoriaMapper;
import br.com.ropalon.tarefas.model.dto.TarefaCategoriaRequest;
import br.com.ropalon.tarefas.model.dto.TarefaCategoriaResponse;
import br.com.ropalon.tarefas.service.TarefaCategoriaService;
import jakarta.validation.Valid;

@RequestMapping("/categoria")
@RestController
public class TarefaCategoriaController {

	private final TarefaCategoriaService service;
	private final TarefaCategoriaMapper mapper;

	public TarefaCategoriaController(TarefaCategoriaService service, TarefaCategoriaMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	@GetMapping
	public List<TarefaCategoriaResponse> todasCategorias() {
		return mapper.toTarefaCategoriaResponseList(service.todasCategorias());
	}

	@GetMapping("/{id}")
	public EntityModel<TarefaCategoriaResponse> umaCategoria(@PathVariable Integer id) {
		var categoriaResponse = mapper.toTarefaCategoriaResponse(service.buscarCategoriaPorId(id));
		
		EntityModel<TarefaCategoriaResponse> categoriaModel = EntityModel.of(categoriaResponse,
				linkTo(methodOn(TarefaCategoriaController.class).umaCategoria(id)).withSelfRel(),
				linkTo(methodOn(TarefaCategoriaController.class).todasCategorias()).withRel("caregorias"));	
		
		return categoriaModel;
	}

	@PostMapping
	public TarefaCategoriaResponse salvarCategoria(@Valid @RequestBody TarefaCategoriaRequest request) {
		var categoria = mapper.toTarefaCategoria(request);
		return mapper.toTarefaCategoriaResponse(service.salvarCategoria(categoria));
	}

	@DeleteMapping("/{id}")
	public void deletarCategoria(@PathVariable Integer id) {
		service.deletarCategoria(id);
	}
	
	@PutMapping("/{id}")
	public TarefaCategoriaResponse atualizarCategoria(@PathVariable Integer id,
			@Valid @RequestBody TarefaCategoriaRequest request) {
		var categoriaAtualizada = mapper.toTarefaCategoria(request);
		return mapper.toTarefaCategoriaResponse(service.atualizarCategoria(id, categoriaAtualizada));
	}
}