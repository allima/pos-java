package br.com.ropalon.tarefas.service;

import br.com.ropalon.tarefas.exception.RoleInvalidaException;
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
        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setRoles(validarRoles(usuario.getRoles()));
        return repository.save(usuario);
    }

    public Usuario atualizarUsuario(Integer id, Usuario usuario) {
        Usuario usuarioExistente = buscarUsuarioPorId(id);
        usuarioExistente.setNome(usuario.getNome());

        if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        usuarioExistente.setRoles(validarRoles(usuario.getRoles()));
        return repository.save(usuarioExistente);
    }

    public void deletarUsuario(Integer id) {
        repository.deleteById(id);
    }

    private Set<Role> validarRoles(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return new HashSet<>(Set.of(obterRolePadrao()));
        }

        Set<Role> validRoles = roles.stream()
                .map(this::resolucaoRoleValida)
                .collect(Collectors.toSet());

        return validRoles;
    }

    private Role resolucaoRoleValida(Role role) {
        return roleRepository.findByName(role.getName())
                .orElseThrow(() -> new RoleInvalidaException("Role inválida: " + role.getName()));
    }

    private Role obterRolePadrao() {
        return roleRepository.findByName(ERole.ROLE_USER).get();
    }
}