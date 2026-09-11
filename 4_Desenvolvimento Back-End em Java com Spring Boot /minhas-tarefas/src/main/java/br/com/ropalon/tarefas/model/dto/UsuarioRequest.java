package br.com.ropalon.tarefas.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
		@NotBlank(message = "O nome do usuário é obrigatório")
		@Size(min = 3, max = 100, message = "O nome do usuário deve ter entre 3 e 100 caracteres")
		String nome,
		@NotBlank(message = "A senha é obrigatória")
		@Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
		String senha) {
}
