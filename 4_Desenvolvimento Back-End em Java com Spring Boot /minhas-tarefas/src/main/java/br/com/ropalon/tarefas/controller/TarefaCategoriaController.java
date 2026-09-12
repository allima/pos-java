package br.com.ropalon.tarefas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ropalon.tarefas.controller.assembler.TarefaCategoriaModelAssembler;
import br.com.ropalon.tarefas.mapper.TarefaCategoriaMapper;
import br.com.ropalon.tarefas.model.TarefaCategoria;
import br.com.ropalon.tarefas.model.dto.TarefaCategoriaRequest;
import br.com.ropalon.tarefas.model.dto.TarefaCategoriaResponse;
import br.com.ropalon.tarefas.service.TarefaCategoriaService;
import jakarta.validation.Valid;

@RequestMapping("/categoria")
@RestController
public class TarefaCategoriaController {

	private final TarefaCategoriaService service;
	private final TarefaCategoriaMapper mapper;
	private final TarefaCategoriaModelAssembler assembler;

	public TarefaCategoriaController(TarefaCategoriaService service, TarefaCategoriaMapper mapper,
			TarefaCategoriaModelAssembler assembler) {
		this.service = service;
		this.mapper = mapper;
		this.assembler = assembler;
	}

	@GetMapping
	public CollectionModel<EntityModel<TarefaCategoriaResponse>> todasCategorias() {
		List<TarefaCategoria> categorias = service.todasCategorias();
		var categoriaModels = categorias.stream().map(assembler::toModel).toList();

		return CollectionModel.of(categoriaModels,
				linkTo(methodOn(TarefaCategoriaController.class).todasCategorias()).withSelfRel());
	}

	@GetMapping("/{id}")
	public EntityModel<TarefaCategoriaResponse> umaCategoria(@PathVariable Integer id) {
		var categoria = service.buscarCategoriaPorId(id);
		return assembler.toModel(categoria);
	}

	@PostMapping
	public ResponseEntity<EntityModel<TarefaCategoriaResponse>> salvarCategoria(@Valid @RequestBody TarefaCategoriaRequest request) {
		var categoria = mapper.toTarefaCategoria(request);
		var categoriaSalva =service.salvarCategoria(categoria);
		var categoriaModel=assembler.toModel(categoriaSalva);
		
		return ResponseEntity.created(categoriaModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(categoriaModel);
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