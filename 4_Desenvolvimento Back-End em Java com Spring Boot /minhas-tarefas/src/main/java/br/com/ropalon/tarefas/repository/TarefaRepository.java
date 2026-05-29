package br.com.ropalon.tarefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ropalon.tarefas.model.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {

}
