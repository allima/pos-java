package br.com.ropalon.tarefas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.ropalon.tarefas.model.Usuario;
import br.com.ropalon.tarefas.model.dto.UsuarioRequest;
import br.com.ropalon.tarefas.model.dto.UsuarioResponse;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

	UsuarioResponse toUsuarioResponse(Usuario usuario);

	List<UsuarioResponse> toUsuarioResponseList(List<Usuario> usuarios);

	@Mapping(target = "id", ignore = true)
	Usuario toUsuario(UsuarioRequest request);
}
