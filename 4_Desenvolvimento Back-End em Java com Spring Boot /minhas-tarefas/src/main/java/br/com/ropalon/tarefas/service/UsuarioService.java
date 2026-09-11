package br.com.ropalon.tarefas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.ropalon.tarefas.model.Usuario;
import br.com.ropalon.tarefas.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioService {

	private final UsuarioRepository repository;

	public UsuarioService(UsuarioRepository repository) {
		this.repository = repository;
	}

	public List<Usuario> todosUsuarios() {
		return repository.findAll();
	}

	public Usuario buscarUsuarioPorId(Integer id) {
		return repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o id: " + id));
	}

	public Usuario salvarUsuario(Usuario usuario) {
		return repository.save(usuario);
	}

	public Usuario atualizarUsuario(Integer id, Usuario usuario) {
		Usuario usuarioExistente = buscarUsuarioPorId(id);
		usuarioExistente.setNome(usuario.getNome());
		usuarioExistente.setSenha(usuario.getSenha());
		return repository.save(usuarioExistente);
	}

	public void deletarUsuario(Integer id) {
		repository.deleteById(id);
	}
}
