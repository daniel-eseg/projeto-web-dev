package br.edu.eseg.petrolifera.oilDynamics.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.eseg.petrolifera.oilDynamics.model.Producao;

public interface ProducaoRepository extends JpaRepository<Producao, Long> {

	@EntityGraph(attributePaths = { "poco", "refinariaDestino" })
	List<Producao> findAllByOrderByDataProducaoDescCodigoAsc();

	boolean existsByCodigo(String codigo);

	boolean existsByCodigoAndIdNot(String codigo, Long id);

	/** RN-04: uma produção por poço e data. */
	boolean existsByPocoIdAndDataProducao(Long pocoId, LocalDate dataProducao);

	boolean existsByPocoIdAndDataProducaoAndIdNot(Long pocoId, LocalDate dataProducao, Long id);

}
