package br.edu.eseg.petrolifera.oilDynamics.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.eseg.petrolifera.oilDynamics.model.PrecoMercado;

public interface PrecoMercadoRepository extends JpaRepository<PrecoMercado, Long> {

	/** RN-10: no máximo um registro por mês; vazio = mês sem cotação. */
	Optional<PrecoMercado> findByMesReferencia(LocalDate mesReferencia);

}
