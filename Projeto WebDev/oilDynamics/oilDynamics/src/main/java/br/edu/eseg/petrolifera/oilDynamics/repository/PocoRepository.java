package br.edu.eseg.petrolifera.oilDynamics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.eseg.petrolifera.oilDynamics.model.Poco;
import br.edu.eseg.petrolifera.oilDynamics.model.StatusPoco;

public interface PocoRepository extends JpaRepository<Poco, Long> {

	/** @EntityGraph carrega o campo junto (um JOIN), evitando uma consulta extra por linha na listagem. */
	@EntityGraph(attributePaths = "campo")
	List<Poco> findAllByOrderByCodigoAsc();

	/** Filtro por status da listagem (RF-12). */
	@EntityGraph(attributePaths = "campo")
	List<Poco> findByStatusPocoOrderByCodigoAsc(StatusPoco statusPoco);

	boolean existsByCodigo(String codigo);

	boolean existsByCodigoAndIdNot(String codigo, Long id);

	/** RN-06: impede excluir um campo que ainda tem poço ativo. */
	boolean existsByCampoIdAndStatusPoco(Long campoId, StatusPoco statusPoco);

}
