package br.com.ropalon.tarefas.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.ropalon.tarefas.exception.TarefaStatusException;
import br.com.ropalon.tarefas.model.Tarefa;
import br.com.ropalon.tarefas.model.TarefaStatus;
import br.com.ropalon.tarefas.repository.TarefaRepository;

@ExtendWith(MockitoExtension.class)
class TarefaServiceMockTest {

	@Mock
	private TarefaRepository repository;
	@InjectMocks
	private TarefaService service;

	@Test
	void naoDeveConcluirTarefaCancelada() {

		var tarefa = new Tarefa();
		tarefa.setId(1);
		tarefa.setStatus(TarefaStatus.CANCELADO);
		tarefa.setDescricao("Tarefa de teste");
		Mockito.when(repository.findById(any())).thenReturn(Optional.of(tarefa));

		Assertions.assertThrows(TarefaStatusException.class, 
				() -> service.concluirTarefaPorId(1));

	}

	@Test
	void naoDeveCancelarTarefaCancelada() {

		var tarefa = new Tarefa();
		tarefa.setId(1);
		tarefa.setStatus(TarefaStatus.CONCLUIDA);
		tarefa.setDescricao("Tarefa de teste");
		Mockito.when(repository.findById(any())).thenReturn(Optional.of(tarefa));
		
		Assertions.assertThrows(TarefaStatusException.class, 
				() -> service.cancelarTarefaPorId(1));

	}

}
