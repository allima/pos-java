package br.com.ropalon.tarefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ropalon.tarefas.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

}
