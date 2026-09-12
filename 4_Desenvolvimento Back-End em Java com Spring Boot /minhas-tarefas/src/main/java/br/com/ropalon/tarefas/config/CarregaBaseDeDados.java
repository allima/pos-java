package br.com.ropalon.tarefas.config;

import java.time.LocalDate;
import java.util.HashSet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.ropalon.tarefas.model.ERole;
import br.com.ropalon.tarefas.model.Tarefa;
import br.com.ropalon.tarefas.model.TarefaCategoria;
import br.com.ropalon.tarefas.model.TarefaStatus;
import br.com.ropalon.tarefas.model.Role;
import br.com.ropalon.tarefas.model.Usuario;
import br.com.ropalon.tarefas.repository.TarefaCategoriaRepository;
import br.com.ropalon.tarefas.repository.TarefaRepository;
import br.com.ropalon.tarefas.repository.RoleRepository;
import br.com.ropalon.tarefas.repository.UsuarioRepository;



@Configuration
@Profile("dev")
public class CarregaBaseDeDados {

	private final UsuarioRepository usuarioRepository;
	private final RoleRepository roleRepository;
	private final TarefaCategoriaRepository categoriaRepository;
	private final TarefaRepository tarefaRepository;
	private final PasswordEncoder passwordEncoder;

	public CarregaBaseDeDados(UsuarioRepository usuarioRepository, RoleRepository roleRepository,
			TarefaCategoriaRepository categoriaRepository, TarefaRepository tarefaRepository,
			PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.roleRepository = roleRepository;
		this.categoriaRepository = categoriaRepository;
		this.tarefaRepository = tarefaRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Bean
	CommandLineRunner executar() {
		return _ -> {
			Usuario usuario = new Usuario();
			usuario.setNome("João");
			usuario.setSenha(passwordEncoder.encode("123456"));

			Role role = roleRepository.findByName(ERole.ROLE_USER).orElseGet(() -> {
				Role novaRole = new Role();
				novaRole.setName(ERole.ROLE_USER);
				return roleRepository.save(novaRole);
			});

			Role role2 = roleRepository.findByName(ERole.ROLE_ADMIN).orElseGet(() -> {
				Role novaRole = new Role();
				novaRole.setName(ERole.ROLE_ADMIN);
				return roleRepository.save(novaRole);
			});
			usuario.setRoles(new HashSet<>(java.util.Set.of(role2,role)));

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

			Tarefa tarefa3 = new Tarefa();
			tarefa3.setDescricao("Estudar Spring Security");
			tarefa3.setDataEntrega(LocalDate.now().plusDays(7));
			tarefa3.setStatus(TarefaStatus.ABERTO);
			tarefa3.setVisivel(true);
			tarefa3.setCategoria(categoria);
			tarefa3.setUsuario(usuario);

			usuarioRepository.save(usuario);
			categoriaRepository.save(categoria);
			tarefaRepository.save(tarefa);
			tarefaRepository.save(tarefa2);
			tarefaRepository.save(tarefa3);

		};
	}
}