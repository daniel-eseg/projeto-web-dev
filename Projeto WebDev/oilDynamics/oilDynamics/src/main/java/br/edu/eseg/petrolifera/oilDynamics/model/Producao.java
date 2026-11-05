package br.edu.eseg.petrolifera.oilDynamics.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "producao",
		// RN-04: no máximo uma produção por poço e data
		uniqueConstraints = @UniqueConstraint(name = "uk_producao_poco_data", columnNames = { "poco_id", "data_producao" }),
		indexes = @Index(name = "idx_producao_data", columnList = "data_producao"))
@Getter
@Setter
@NoArgsConstructor
public class Producao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Código de negócio vindo do legado (ex.: PR000001). */
	@NotBlank(message = "Informe o código.")
	@Size(max = 20, message = "O código tem no máximo 20 caracteres.")
	@Column(nullable = false, unique = true, length = 20)
	private String codigo;

	/** RN-05: volumes e custos não podem ser negativos. */
	@NotNull(message = "Informe o volume de óleo.")
	@PositiveOrZero(message = "O volume de óleo não pode ser negativo.")
	@Column(nullable = false)
	private Double volumeOleoBarris;

	@NotNull(message = "Informe o volume de gás.")
	@PositiveOrZero(message = "O volume de gás não pode ser negativo.")
	@Column(nullable = false)
	private Double volumeGasMetrosCubicos;

	/** RN-05: eficiência entre 0 e 100. */
	@NotNull(message = "Informe a eficiência.")
	@PositiveOrZero(message = "A eficiência deve estar entre 0 e 100.")
	@DecimalMax(value = "100", message = "A eficiência deve estar entre 0 e 100.")
	@Column(nullable = false)
	private Double eficienciaPercentual;

	@NotNull(message = "Informe o custo operacional.")
	@PositiveOrZero(message = "O custo operacional não pode ser negativo.")
	@Column(nullable = false)
	private Double custoOperacional;

	@NotNull(message = "Informe a data da produção.")
	@Column(nullable = false)
	private LocalDate dataProducao;

	/** Substitui o antigo idPoco. */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "poco_id", nullable = false)
	private Poco poco;

	/** Destino opcional da produção (RF-05). */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "refinaria_destino_id")
	private Refinaria refinariaDestino;

}
