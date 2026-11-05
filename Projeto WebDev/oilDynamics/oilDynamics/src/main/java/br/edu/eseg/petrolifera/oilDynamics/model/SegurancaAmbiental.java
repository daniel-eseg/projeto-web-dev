package br.edu.eseg.petrolifera.oilDynamics.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "seguranca_ambiental")
@Getter
@Setter
@NoArgsConstructor
public class SegurancaAmbiental {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Código de negócio vindo do legado (antigo idEvento). */
	@Column(nullable = false, unique = true, length = 20)
	private String codigo;

	@Column(nullable = false, length = 50)
	private String tipoEvento;

	@Column(nullable = false, length = 20)
	private String gravidade;

	@Column(nullable = false)
	private Double custoEstimado;

	@Column(nullable = false)
	private LocalDate dataEvento;

	/** Poço onde o evento ocorreu, opcional (RF-06). */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "poco_id")
	private Poco poco;

}
