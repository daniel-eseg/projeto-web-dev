package br.edu.eseg.petrolifera.oilDynamics.service;

/** Lançada quando um id da URL não existe no banco (vira uma página 404). */
public class RegistroNaoEncontradoException extends RuntimeException {

	public RegistroNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

}
