package br.com.ropalon.tarefas.controller;

import br.com.ropalon.tarefas.model.Tarefa;
import br.com.ropalon.tarefas.repository.TarefaRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Map;

@RestController
public class TarefaController {

    private final TarefaRepository tarefaRepository;

    public TarefaController(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    @GetMapping("/tarefa")
    public List<Tarefa> todasTarefas(@RequestParam Map<String, String> parametros) {
        if (parametros.isEmpty()) {
            return tarefaRepository.findAll();
        }
        var descricao = parametros.get("descricao");
        return tarefaRepository.findByDescricaoLikeIgnoreCase("%"+descricao+"%");
    }

    @GetMapping("/tarefa/{id}")
    public Tarefa umaTarefa(@PathVariable Integer id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com id: " + id));
    }

    @PostMapping("/tarefa")
    public Tarefa criarTarefa(@RequestBody Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    @DeleteMapping("/tarefa/{id}")
    public void deletarTarefa(@PathVariable Integer id) {
        tarefaRepository.deleteById(id);
    }

    @PutMapping("/tarefa/{id}")
    public Tarefa atualizarTarefa(@PathVariable Integer id, @RequestBody Tarefa tarefaAtual) {
        return tarefaRepository.findById(id)
                .map(tarefa -> {
                    tarefa.setCategoria(tarefaAtual.getCategoria());
                    tarefa.setDescricao(tarefaAtual.getDescricao());
                    tarefa.setStatus(tarefaAtual.getStatus());
                    tarefa.setDataEntrega(tarefaAtual.getDataEntrega());
                    tarefa.setVisivel(tarefaAtual.isVisivel());
                    return tarefaRepository.save(tarefa);
                })
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com id: " + id));

    }

}
