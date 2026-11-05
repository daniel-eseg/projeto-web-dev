package br.edu.eseg.petrolifera.oilDynamics.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "campo")
@Getter
@Setter
@NoArgsConstructor
public class Campo {

	/** Chave substituta: não carrega significado (LA-01). */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Código de negócio vindo do legado (ex.: CP0001), único (RF-01). */
	@NotBlank(message = "Informe o código.")
	@Size(max = 20, message = "O código tem no máximo 20 caracteres.")
	@Column(nullable = false, unique = true, length = 20)
	private String codigo;

	@NotBlank(message = "Informe o nome da bacia.")
	@Size(max = 100, message = "O nome da bacia tem no máximo 100 caracteres.")
	@Column(nullable = false, length = 100)
	private String nomeBacia;

	/** UF com 2 letras maiúsculas (RF-01). */
	@NotBlank(message = "Informe a UF.")
	@Pattern(regexp = "[A-Z]{2}", message = "A UF deve ter 2 letras maiúsculas (ex.: RJ).")
	@Column(nullable = false, length = 2)
	private String estado;

	@NotBlank(message = "Informe o ambiente.")
	@Size(max = 30)
	@Column(nullable = false, length = 30)
	private String ambiente;

	@NotBlank(message = "Informe o status.")
	@Size(max = 30)
	@Column(nullable = false, length = 30)
	private String status;

	/** Lado inverso da composição Campo 1-* Poco; excluir o campo exclui os poços (RN-06). */
	@OneToMany(mappedBy = "campo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Poco> pocos = new ArrayList<>();

}
