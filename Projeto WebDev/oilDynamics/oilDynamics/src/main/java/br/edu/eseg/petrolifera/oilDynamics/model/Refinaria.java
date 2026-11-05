package br.edu.eseg.petrolifera.oilDynamics.model;

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
@Table(name = "refinaria")
@Getter
@Setter
@NoArgsConstructor
public class Refinaria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Código de negócio vindo do legado (ex.: RF001). */
	@Column(nullable = false, unique = true, length = 20)
	private String codigo;

	@Column(nullable = false, length = 2)
	private String estado;

	@Column(nullable = false)
	private Integer capacidadeBarrisPorDia;

	@Column(nullable = false, length = 30)
	private String statusRefinaria;

}
