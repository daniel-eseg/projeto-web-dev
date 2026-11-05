package br.edu.eseg.petrolifera.oilDynamics.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** produto, volume e data vêm de MovimentacaoProduto (não são repetidos aqui). */
@Entity
@Table(name = "transporte_logistica")
@Getter
@Setter
@NoArgsConstructor
public class TransporteLogistica extends MovimentacaoProduto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Código de negócio vindo do legado (antigo idTransporte). */
	@Column(nullable = false, unique = true, length = 20)
	private String codigo;

	@Column(nullable = false, length = 30)
	private String modalTransporte;

	@Column(nullable = false)
	private Double custoTransporte;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private StatusEntrega statusEntrega;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "refinaria_origem_id")
	private Refinaria refinariaOrigem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "venda_id")
	private Venda venda;

	/** RF-09. TODO: implementar quando a unidade de volume for definida (V-04 / RN-08). */
	public double calcularCustoTransporte(PrecoMercado precoMercado) {
		return 0;
	}

}
