package br.com.ropalon.tarefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ropalon.tarefas.model.TarefaCategoria;

public interface TarefaCategoriaRepository extends JpaRepository<TarefaCategoria, Integer> {

}
