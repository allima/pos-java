package br.com.ropalon.tarefas.repository;

import br.com.ropalon.tarefas.model.TarefaCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ropalon.tarefas.model.Tarefa;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {

    public List<Tarefa> findByDescricao(String descricao);

    public List<Tarefa> findByDescricaoLikeIgnoreCase(String descricao);

    public List<Tarefa> findByCategoria(TarefaCategoria categoria);

    @Query("select t from Tarefa t inner join t.categoria c where c.nome = :nomeCategoria")
    public List<Tarefa> findByNomeCategoria(String nomeCategoria);

    public List<Tarefa> terafasPorCategoria(String nomeCategoria);
}
