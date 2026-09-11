package br.com.ropalon.tarefas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.ropalon.tarefas.model.TarefaCategoria;
import br.com.ropalon.tarefas.repository.TarefaCategoriaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TarefaCategoriaService {

	private final TarefaCategoriaRepository repository;

	public TarefaCategoriaService(TarefaCategoriaRepository repository) {
		this.repository = repository;
	}

	public List<TarefaCategoria> todasCategorias() {
		return repository.findAll();
	}

	public TarefaCategoria buscarCategoriaPorId(Integer id) {
		return repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com o id: " + id));
	}

	public TarefaCategoria salvarCategoria(TarefaCategoria categoria) {
		return repository.save(categoria);
	}

	public void deletarCategoria(Integer id) {
		repository.deleteById(id);
	}

	public TarefaCategoria atualizarCategoria(Integer id, TarefaCategoria categoria) {
		TarefaCategoria categoriaExistente = buscarCategoriaPorId(id);
		categoriaExistente.setNome(categoria.getNome());
		return repository.save(categoriaExistente);
	}
}