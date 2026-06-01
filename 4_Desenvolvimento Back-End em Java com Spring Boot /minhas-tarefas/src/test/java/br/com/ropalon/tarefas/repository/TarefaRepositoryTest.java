package br.com.ropalon.tarefas.repository;

import br.com.ropalon.tarefas.model.Tarefa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TarefaRepositoryTest {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Test
    void findByDescricao() {

    }

    @Test
    void findByDescricaoLikeIgnoreCase() {
     List<Tarefa> tarefas = tarefaRepository.findByDescricaoLikeIgnoreCase("%spring%");
     Assertions.assertNotNull(tarefas);
     Assertions.assertEquals(2, tarefas.size());
    }

    @Test
    void findByCategoria() {

    }

    @Test
    void findByNomeCategoria() {
        List<Tarefa> tarefas = tarefaRepository.findByNomeCategoria("Estudos");
        Assertions.assertNotNull(tarefas);
        Assertions.assertEquals(2, tarefas.size());

    }

    @Test
    void terafasPorCategoria() {
        List<Tarefa> tarefas = tarefaRepository.terafasPorCategoria("Estudos");
        Assertions.assertNotNull(tarefas);
        Assertions.assertEquals(2, tarefas.size());

    }
}