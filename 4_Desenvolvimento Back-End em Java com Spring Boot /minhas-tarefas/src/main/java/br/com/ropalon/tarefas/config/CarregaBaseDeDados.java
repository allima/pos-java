package br.com.ropalon.tarefas.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.ropalon.tarefas.model.Tarefa;
import br.com.ropalon.tarefas.model.TarefaCategoria;
import br.com.ropalon.tarefas.model.TarefaStatus;
import br.com.ropalon.tarefas.model.Usuario;
import br.com.ropalon.tarefas.repository.TarefaCategoriaRepository;
import br.com.ropalon.tarefas.repository.TarefaRepository;
import br.com.ropalon.tarefas.repository.UsuarioRepository;

@Configuration
@Profile("dev")
public class CarregaBaseDeDados {

	private final UsuarioRepository usuarioRepository;
	private final TarefaCategoriaRepository categoriaRepository;
	private final TarefaRepository tarefaRepository;

	public CarregaBaseDeDados(UsuarioRepository usuarioRepository, TarefaCategoriaRepository categoriaRepository,
			TarefaRepository tarefaRepository) {
		this.usuarioRepository = usuarioRepository;
		this.categoriaRepository = categoriaRepository;
		this.tarefaRepository = tarefaRepository;
	}

	@Bean
	CommandLineRunner executar() {
		return _ -> {
			Usuario usuario = new Usuario();
			usuario.setNome("João");
			usuario.setSenha("123456");

			TarefaCategoria categoria = new TarefaCategoria();
			categoria.setNome("Estudos");

			Tarefa tarefa = new Tarefa();
			tarefa.setDescricao("Estudar Spring Boot");
			tarefa.setDataEntrega(LocalDate.now().plusDays(7));
			tarefa.setStatus(TarefaStatus.ABERTO);
			tarefa.setVisivel(true);
			tarefa.setCategoria(categoria);
			tarefa.setUsuario(usuario);

			Tarefa tarefa2 = new Tarefa();
			tarefa2.setDescricao("Estudar Spring Data JPA");
			tarefa2.setDataEntrega(LocalDate.now().plusDays(7));
			tarefa2.setStatus(TarefaStatus.ABERTO);
			tarefa2.setVisivel(true);
			tarefa2.setCategoria(categoria);
			tarefa2.setUsuario(usuario);

			usuarioRepository.save(usuario);
			categoriaRepository.save(categoria);
			tarefaRepository.save(tarefa);
			tarefaRepository.save(tarefa2);

		};
	}
}
