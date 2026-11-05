package br.edu.eseg.petrolifera.oilDynamics.model;

/** Domínio do status de entrega do transporte (RN-09). */
public enum StatusEntrega {
	EM_TRANSITO("Em trânsito"),
	CONCLUIDA("Concluída"),
	ATRASADA("Atrasada");

	private final String descricao;

	StatusEntrega(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
