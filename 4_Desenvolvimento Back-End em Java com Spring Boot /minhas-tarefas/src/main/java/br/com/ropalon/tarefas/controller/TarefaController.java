package br.com.ropalon.tarefas.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ropalon.tarefas.mapper.TarefasMapper;
import br.com.ropalon.tarefas.model.Tarefa;
import br.com.ropalon.tarefas.model.dto.TarefaResponse;
import br.com.ropalon.tarefas.service.TarefaService;
import jakarta.validation.Valid;

@RequestMapping("/tarefa")
@RestController
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
			tarefas= service.todasTarefas();
		}else {
		var descricao = parametros.get("descricao");
		tarefas= service.todasTarefasPorDescricao(descricao);
		}
		
		return mapper.toTarefasResponseList(tarefas);
	}

	@GetMapping("/{id}")
	public TarefaResponse umaTarefa(@PathVariable Integer id) {

		var tarefa = service.buscarTarefaPorId(id);
		return mapper.toTarefaResponse(tarefa);
	}

	@PostMapping
	public TarefaResponse criarTarefa(@Valid @RequestBody Tarefa tarefa) {
		return mapper.toTarefaResponse(service.salvarTarefa(tarefa));
	}

	@DeleteMapping("/{id}")
	public void deletarTarefa(@PathVariable Integer id) {
		service.deletarTarefa(id);
	}

//	@PutMapping("/{id}")
//	public Tarefa atualizarTarefa(@PathVariable Integer id, @RequestBody Tarefa tarefaAtual) {
//		return service.buscarTarefaPorId(id).map(tarefa -> {
//			tarefa.setCategoria(tarefaAtual.getCategoria());
//			tarefa.setDescricao(tarefaAtual.getDescricao());
//			tarefa.setStatus(tarefaAtual.getStatus());
//			tarefa.setDataEntrega(tarefaAtual.getDataEntrega());
//			tarefa.setVisivel(tarefaAtual.isVisivel());
//			return service.save(tarefa);
//		}).orElseThrow(() -> new RuntimeException("Tarefa não encontrada com id: " + id));
//
//	}

}
