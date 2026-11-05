package br.edu.eseg.petrolifera.oilDynamics.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** produto, volume e data vêm de MovimentacaoProduto (não são repetidos aqui). */
@Entity
@Table(name = "venda")
@Getter
@Setter
@NoArgsConstructor
public class Venda extends MovimentacaoProduto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Código de negócio vindo do legado (antigo idVenda). */
	@Column(nullable = false, unique = true, length = 20)
	private String codigo;

	@Column(nullable = false)
	private Double valorTotal;

	@Column(nullable = false, length = 30)
	private String mercado;

	/** Agregação: excluir a venda não exclui os transportes (sem cascade). */
	@OneToMany(mappedBy = "venda")
	private List<TransporteLogistica> transportes = new ArrayList<>();

	/** RF-09. TODO: implementar quando a unidade de volume for definida (V-04 / RN-08). */
	public double calcularValorTotal(PrecoMercado precoMercado) {
		return 0;
	}

}
