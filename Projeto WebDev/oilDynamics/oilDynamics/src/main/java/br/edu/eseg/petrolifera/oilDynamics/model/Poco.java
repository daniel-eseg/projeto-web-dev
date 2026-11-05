package br.edu.eseg.petrolifera.oilDynamics.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "poco", indexes = @Index(name = "idx_poco_status", columnList = "status_poco"))
@Getter
@Setter
@NoArgsConstructor
public class Poco {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Código de negócio vindo do legado (ex.: PO00001). */
	@NotBlank(message = "Informe o código.")
	@Size(max = 20, message = "O código tem no máximo 20 caracteres.")
	@Column(nullable = false, unique = true, length = 20)
	private String codigo;

	@NotBlank(message = "Informe o tipo do poço.")
	@Size(max = 30)
	@Column(nullable = false, length = 30)
	private String tipoPoco;

	/** Vazão negativa é rejeitada (RF-02). */
	@NotNull(message = "Informe a vazão.")
	@PositiveOrZero(message = "A vazão não pode ser negativa.")
	@Column(nullable = false)
	private Double vazaoBarrisPorDia;

	@NotNull(message = "Informe o status.")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private StatusPoco statusPoco;

	@NotNull(message = "Informe a data de início.")
	@Column(nullable = false)
	private LocalDate dataInicio;

	/** Lado proprietário da associação: a FK campo_id fica na tabela poco. Substitui o antigo idCampo. */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "campo_id", nullable = false)
	private Campo campo;

	@OneToMany(mappedBy = "poco", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Producao> producoes = new ArrayList<>();

}
