package br.com.ropalon.tarefas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.ropalon.tarefas.exception.TarefaStatusException;
import br.com.ropalon.tarefas.model.Tarefa;
import br.com.ropalon.tarefas.model.TarefaStatus;
import br.com.ropalon.tarefas.repository.TarefaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TarefaService {

	private final TarefaRepository tarefaRepository;

	public TarefaService(TarefaRepository tarefaRepository) {
		this.tarefaRepository = tarefaRepository;
	}

	public List<Tarefa> todasTarefas() {
		return tarefaRepository.findAll();
	}

	public List<Tarefa> todasTarefasPorDescricao(String descricao) {
		return tarefaRepository.findByDescricaoLikeIgnoreCase("%" + descricao + "%");
	}

	public Tarefa buscarTarefaPorId(Integer id) {
		return tarefaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com o id: " + id));
	}

	public Tarefa salvarTarefa(Tarefa tarefa) {
		return tarefaRepository.save(tarefa);
	}

	public Tarefa atualizarTarefa(Tarefa tarefa) {
		return tarefaRepository.save(tarefa);
	}

	public void deletarTarefa(Integer id) {
		tarefaRepository.deleteById(id);
	}

	public Tarefa iniciarTarefaPorId(Integer id) {
		Tarefa tarefa = buscarTarefaPorId(id);
		if(!TarefaStatus.ABERTO.equals(tarefa.getStatus())) {
			throw new TarefaStatusException("A tarefa não está no status ABERTO e não pode ser iniciada.");
		}
		tarefa.setStatus(TarefaStatus.EM_ANDAMENTO);
		tarefaRepository.save(tarefa);
		return tarefa;
	}
	
	public Tarefa concluirTarefaPorId(Integer id) {
		Tarefa tarefa = buscarTarefaPorId(id);
		if (TarefaStatus.CANCELADO.equals(tarefa.getStatus())) {
			throw new TarefaStatusException("A tarefa está cancelada e não pode ser concluída.");
		}
		if (TarefaStatus.CONCLUIDA.equals(tarefa.getStatus())) {
			throw new TarefaStatusException("A tarefa já está concluída e não pode ser concluída novamente.");
		}
		tarefa.setStatus(TarefaStatus.CONCLUIDA);
		tarefaRepository.save(tarefa);
		return tarefa;
	}

	
	public Tarefa cancelarTarefaPorId(Integer id) {
		Tarefa tarefa = buscarTarefaPorId(id);
		if(TarefaStatus.CONCLUIDA.equals(tarefa.getStatus())) {
			throw new TarefaStatusException("A tarefa não está no status CONCLUIDA e não pode ser CANCELADA.");
		}
		tarefa.setStatus(TarefaStatus.CANCELADO);
		tarefaRepository.save(tarefa);
		return tarefa;
	}
	
	
	
}
