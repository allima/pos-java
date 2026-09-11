package br.com.ropalon.tarefas.repository;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.ropalon.tarefas.model.Tarefa;
import br.com.ropalon.tarefas.model.TarefaCategoria;

@SpringBootTest
class TarefaRepositoryTest {

	@Autowired
	private TarefaRepository tarefaRepository;

	@Autowired
	private TarefaCategoriaRepository tarefaCategoriaRepository;

	@Test
	void findByDescricao() {
		List<Tarefa> tarefas = tarefaRepository.findByDescricao("Estudar Spring Boot");
		Assertions.assertNotNull(tarefas);
		Assertions.assertEquals(1, tarefas.size());
		Assertions.assertEquals("Estudar Spring Boot", tarefas.get(0).getDescricao());
	}

	@Test
	void findByDescricaoLikeIgnoreCase() {
		List<Tarefa> tarefas = tarefaRepository.findByDescricaoLikeIgnoreCase("%spring%");
		Assertions.assertNotNull(tarefas);
		Assertions.assertEquals(3, tarefas.size());
	}

	@Test
	void findByCategoria() {
		TarefaCategoria categoria = tarefaCategoriaRepository.findAll().get(0);
		List<Tarefa> tarefas = tarefaRepository.findByCategoria(categoria);
		Assertions.assertNotNull(tarefas);
		Assertions.assertEquals(3, tarefas.size());
	}

	@Test
	void findByNomeCategoria() {
		List<Tarefa> tarefas = tarefaRepository.findByNomeCategoria("Estudos");
		Assertions.assertNotNull(tarefas);
		Assertions.assertEquals(3, tarefas.size());
	}

	@Test
	void terafasPorCategoria() {
		List<Tarefa> tarefas = tarefaRepository.terafasPorCategoria("Estudos");
		Assertions.assertNotNull(tarefas);
		Assertions.assertEquals(3, tarefas.size());
	}
}
