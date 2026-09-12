package br.com.ropalon.tarefas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ropalon.tarefas.controller.assembler.TarefaModelAssembler;
import br.com.ropalon.tarefas.mapper.TarefasMapper;
import br.com.ropalon.tarefas.model.Tarefa;
import br.com.ropalon.tarefas.model.dto.TarefaRequest;
import br.com.ropalon.tarefas.model.dto.TarefaResponse;
import br.com.ropalon.tarefas.service.TarefaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tarefa")
public class TarefaController {

	private final TarefaService service;
	private final TarefasMapper mapper;
	private final TarefaModelAssembler assembler;

	public TarefaController(TarefaService service, TarefasMapper mapper, TarefaModelAssembler assembler) {
		this.service = service;
		this.mapper = mapper;
		this.assembler = assembler;
	}

	@GetMapping
	public CollectionModel<EntityModel<TarefaResponse>> todasTarefas(@RequestParam Map<String, String> parametros) {
		List<Tarefa> tarefas = List.of();
		if (parametros.isEmpty()) {
			tarefas = service.todasTarefas();
		} else {
			var descricao = parametros.get("descricao");
			tarefas = service.todasTarefasPorDescricao(descricao);
		}

		var tarefaModels = tarefas.stream().map(assembler::toModel).toList();

		return CollectionModel.of(tarefaModels,
				linkTo(methodOn(TarefaController.class).todasTarefas(new HashMap<>())).withSelfRel());
	}

	@GetMapping("/{id}")
	public EntityModel<TarefaResponse> umaTarefa(@PathVariable Integer id) {
		var tarefa = service.buscarTarefaPorId(id);
		return assembler.toModel(tarefa);
	}

	@PostMapping
	public ResponseEntity<EntityModel<TarefaResponse>> salvarTarefa(@Valid @RequestBody TarefaRequest tarefa) {
		var tarefaNova = mapper.toTarefa(tarefa);
		var tarefaSalva = service.salvarTarefa(tarefaNova);
		var tarefaModel = assembler.toModel(tarefaSalva);
		return ResponseEntity.created(tarefaModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(tarefaModel);
	}

	@DeleteMapping("/{id}")
	public void deletarTarefa(@PathVariable Integer id) {
		service.deletarTarefa(id);
	}

	@PutMapping("/{id}")
	public TarefaResponse atualizarTarefa(@PathVariable Integer id, @Valid @RequestBody TarefaRequest tarefa) {
		var tarefaAtualizada = mapper.toTarefa(tarefa);
		return mapper.toTarefaResponse(service.atualizarTarefa(id, tarefaAtualizada));
	}

	@PutMapping("/{id}/iniciar")
	public EntityModel<TarefaResponse> iniciarTarefa(@PathVariable Integer id) {
		var tarefa = service.iniciarTarefaPorId(id);
		return assembler.toModel(tarefa);
	}

	@PutMapping("/{id}/concluir")
	public EntityModel<TarefaResponse> concluirTarefa(@PathVariable Integer id) {
		var tarefa = service.concluirTarefaPorId(id);
		return assembler.toModel(tarefa);
	}

	@PutMapping("/{id}/cancelar")
	public EntityModel<TarefaResponse> cancelarTarefa(@PathVariable Integer id) {
		var tarefa = service.cancelarTarefaPorId(id);
		return assembler.toModel(tarefa);
	}

}