package br.edu.eseg.petrolifera.oilDynamics.service;

/**
 * Violação de uma regra de negócio (ex.: transição de status inválida, código repetido).
 * "campo" é o nome do atributo do formulário onde a mensagem deve aparecer;
 * quando for null, a mensagem é geral (aparece no topo do formulário).
 */
public class RegraNegocioException extends RuntimeException {

	private final String campo;

	public RegraNegocioException(String mensagem) {
		this(null, mensagem);
	}

	public RegraNegocioException(String campo, String mensagem) {
		super(mensagem);
		this.campo = campo;
	}

	public String getCampo() {
		return campo;
	}

}
