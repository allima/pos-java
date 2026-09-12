package br.com.ropalon.tarefas.service;

import br.com.ropalon.tarefas.model.ERole;
import br.com.ropalon.tarefas.model.Role;
import br.com.ropalon.tarefas.model.Usuario;
import br.com.ropalon.tarefas.repository.RoleRepository;
import br.com.ropalon.tarefas.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> todosUsuarios() {
        return repository.findAll();
    }

    public Usuario buscarUsuarioPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o id: " + id));
    }

    public Usuario salvarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setRoles(normalizarRoles(usuario.getRoles()));
        atribuirRolePadraoSeNecessario(usuario);
        return repository.save(usuario);
    }

    public Usuario atualizarUsuario(Integer id, Usuario usuario) {
        Usuario usuarioExistente = buscarUsuarioPorId(id);
        usuarioExistente.setNome(usuario.getNome());
        usuarioExistente.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioExistente.setRoles(normalizarRoles(usuario.getRoles()));
        atribuirRolePadraoSeNecessario(usuarioExistente);
        return repository.save(usuarioExistente);
    }

    public void deletarUsuario(Integer id) {
        repository.deleteById(id);
    }

    private void atribuirRolePadraoSeNecessario(Usuario usuario) {
        if (usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
            var rolePadrao = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(ERole.ROLE_USER);
                        return roleRepository.save(role);
                    });
            usuario.setRoles(new HashSet<>(Set.of(rolePadrao)));
        }
    }

    private Set<Role> normalizarRoles(Set<Role> roles) {
        return roles.stream()
                .map(this::resolverRolePersistida)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toCollection(HashSet::new));
    }

    private Role resolverRolePersistida(Role role) {
        if (role.getId() != null) {
            return roleRepository.findById(role.getId()).orElseGet(() -> buscarOuSalvarRole(role.getName()));
        }
        return buscarOuSalvarRole(role.getName());
    }

    private Role buscarOuSalvarRole(ERole name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role novaRole = new Role();
                    novaRole.setName(name);
                    return roleRepository.save(novaRole);
                });
    }
}
