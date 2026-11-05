package br.edu.eseg.petrolifera.oilDynamics.model;

/** Lista única de produtos para venda e transporte (RN-07). */
public enum Produto {
	PETROLEO("Petróleo"),
	GAS("Gás"),
	GLP("GLP"),
	DIESEL("Diesel"),
	GASOLINA("Gasolina");

	private final String descricao;

	Produto(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
