package br.com.ropalon.tarefas.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import br.com.ropalon.tarefas.exception.TarefaStatusException;
import br.com.ropalon.tarefas.model.Tarefa;
import br.com.ropalon.tarefas.model.TarefaStatus;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class TarefaServiceIntegrationTest {
	
	@Autowired
	private  TarefaService tarefaService ;
	

	@Test
	void deveIniciarTarefaPorId() {
		Tarefa tarefaIniciada = tarefaService.iniciarTarefaPorId(3);
		Assertions.assertEquals(tarefaIniciada.getStatus(), TarefaStatus.EM_ANDAMENTO);
	}

	
	
	@Test
	void naoDeveIniciarTarefaConcluida() {
		Tarefa tarefa = tarefaService.buscarTarefaPorId(3);
		tarefa.setStatus(TarefaStatus.CONCLUIDA);
		tarefaService.salvarTarefa(tarefa);

		Assertions.assertThrows(TarefaStatusException.class, () -> 
			tarefaService.iniciarTarefaPorId(3)
		);
	}
	
	
	@Test
	void naoDeveConcluirTarefaConcluida() {
		Tarefa tarefa = tarefaService.buscarTarefaPorId(3);
		tarefa.setStatus(TarefaStatus.CONCLUIDA);
		tarefaService.salvarTarefa(tarefa);

		Assertions.assertThrows(TarefaStatusException.class, () -> 
			tarefaService.concluirTarefaPorId(3)
		);
	}
	
	
	@Test
	void naoDeveCancelarTarefaConcluida() {
		Tarefa tarefa = tarefaService.buscarTarefaPorId(3);
		tarefa.setStatus(TarefaStatus.CONCLUIDA);
		tarefaService.salvarTarefa(tarefa);

		Assertions.assertThrows(TarefaStatusException.class, () -> 
			tarefaService.cancelarTarefaPorId(3)
		);
	}

	@Test
	void deveConcluirTarefaPorId() {
		// a massa de dados (perfil dev) carrega a tarefa com id 3 como ABERTO
		Tarefa tarefa = tarefaService.buscarTarefaPorId(3);
		Assertions.assertNotEquals(TarefaStatus.CONCLUIDA, tarefa.getStatus());
		Tarefa tarefaConcluida = tarefaService.concluirTarefaPorId(3);
		Assertions.assertEquals(TarefaStatus.CONCLUIDA, tarefaConcluida.getStatus());
	}

	@Test
	void deveCancelarTarefaPorId() {
		Tarefa tarefa = tarefaService.buscarTarefaPorId(3);
		Assertions.assertNotEquals(TarefaStatus.CANCELADO, tarefa.getStatus());
		Tarefa tarefaCancelada = tarefaService.cancelarTarefaPorId(3);
		Assertions.assertEquals(TarefaStatus.CANCELADO, tarefaCancelada.getStatus());
	}
	
	
	
	
	
}
