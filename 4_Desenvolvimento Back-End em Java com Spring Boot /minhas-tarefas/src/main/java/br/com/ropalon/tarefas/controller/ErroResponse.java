package br.com.ropalon.tarefas.controller;

public record ErroResponse(String campo, String mensagem) {

	public ErroResponse(String mensagem) {
		this(null, mensagem);		
	}
	
	public ErroResponse(String campo, String mensagem) {
		this.campo = campo;
		this.mensagem = mensagem;
	}
	

}