package br.com.ropalon.tarefas.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UsuarioRequest(
        Integer id,
        @NotBlank(message = "O nome do usuário é obrigatório")
        @Size(max = 100, message = "O nome do usuário deve ter ate 100 caracteres")
        @Column(unique = true,  length = 100)
        String nome,
        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        @Column(nullable = false)
        String senha,
        Set<RoleRequest> roles
) {
}
