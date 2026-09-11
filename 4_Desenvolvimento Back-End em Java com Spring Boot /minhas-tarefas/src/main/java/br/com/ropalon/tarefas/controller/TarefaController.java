package br.com.ropalon.tarefas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	public TarefaController(TarefaService service, TarefasMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	@GetMapping
	public List<TarefaResponse> todasTarefas(@RequestParam Map<String, String> parametros) {
		List<Tarefa> tarefas = List.of();
		if (parametros.isEmpty()) {
			tarefas = service.todasTarefas();
		} else {
			var descricao = parametros.get("descricao");
			tarefas = service.todasTarefasPorDescricao(descricao);
		}

		return mapper.toTarefasResponseList(tarefas);
	}

	@GetMapping("/{id}")
	public EntityModel<TarefaResponse> umaTarefa(@PathVariable Integer id) {

		var tarefa = service.buscarTarefaPorId(id);
		var tarefaref  = mapper.toTarefaResponse(tarefa);
		
		EntityModel<TarefaResponse> tarefaModel = EntityModel.of(tarefaref,
				linkTo(methodOn(TarefaController.class).umaTarefa(id)).withSelfRel(),
				linkTo(methodOn(TarefaController.class).todasTarefas(new HashMap<>())).withRel("tarefas"),
				linkTo(methodOn(TarefaCategoriaController.class).umaCategoria(tarefaref.categoriaId())).withRel("categoriax"),
				linkTo(methodOn(UsuarioController.class).umUsuario(tarefaref.usuarioId())).withRel("usuariox"));
		 
		 return tarefaModel;
	}

	@PostMapping
	public TarefaResponse salvarTarefa(@Valid @RequestBody TarefaRequest tarefa) {
		var tarefaNova = mapper.toTarefa(tarefa);

		return mapper.toTarefaResponse(service.salvarTarefa(tarefaNova));
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

}