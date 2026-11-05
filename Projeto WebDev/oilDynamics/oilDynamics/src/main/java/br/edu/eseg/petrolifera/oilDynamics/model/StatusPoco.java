package br.edu.eseg.petrolifera.oilDynamics.model;

/** Domínio do status do poço (RN-01) e suas transições válidas (RN-02). */
public enum StatusPoco {
	ATIVO("Ativo"),
	MANUTENCAO("Manutenção"),
	PARADO("Parado");

	private final String descricao;

	StatusPoco(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	/**
	 * RN-02: Ativo -> Manutenção ou Parado; Manutenção -> Ativo ou Parado; Parado -> somente Manutenção.
	 * Manter o mesmo status é sempre permitido (edição de outros campos).
	 */
	public boolean podeMudarPara(StatusPoco novo) {
		if (novo == this) {
			return true;
		}
		switch (this) {
			case ATIVO:
				return novo == MANUTENCAO || novo == PARADO;
			case MANUTENCAO:
				return novo == ATIVO || novo == PARADO;
			case PARADO:
				return novo == MANUTENCAO;
			default:
				return false;
		}
	}
}
