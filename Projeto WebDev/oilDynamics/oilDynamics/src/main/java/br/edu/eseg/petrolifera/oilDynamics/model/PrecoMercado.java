package br.edu.eseg.petrolifera.oilDynamics.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "preco_mercado")
@Getter
@Setter
@NoArgsConstructor
public class PrecoMercado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Double precoBrentDolaresPorBarril;

	@Column(nullable = false)
	private Double taxaCambioReaisPorDolar;

	@Column(nullable = false)
	private Double precoDieselReaisPorLitro;

	/** No máximo um registro por mês (RN-10). Guardar sempre o dia 1 do mês. */
	@Column(nullable = false, unique = true)
	private LocalDate mesReferencia;

}
