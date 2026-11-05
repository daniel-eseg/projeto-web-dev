package br.edu.eseg.petrolifera.oilDynamics.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * Atributos comuns a Venda e TransporteLogistica (LA-06 / RF-07).
 * Com @MappedSuperclass esta classe não vira tabela: os campos dela são criados
 * dentro da tabela de cada subclasse (venda e transporte_logistica).
 */
@MappedSuperclass
@Getter
@Setter
public abstract class MovimentacaoProduto {

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Produto produto;

	/** Deve ser maior que zero (RN-08). */
	@Column(nullable = false)
	private Double volume;

	@Column(name = "data_movimentacao", nullable = false)
	private LocalDate data;

}
