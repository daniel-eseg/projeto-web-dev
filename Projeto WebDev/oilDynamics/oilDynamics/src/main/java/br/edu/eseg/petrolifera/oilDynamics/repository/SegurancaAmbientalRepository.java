package br.edu.eseg.petrolifera.oilDynamics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.eseg.petrolifera.oilDynamics.model.SegurancaAmbiental;

public interface SegurancaAmbientalRepository extends JpaRepository<SegurancaAmbiental, Long> {

	/** Consulta de eventos por poço (RF-06). */
	List<SegurancaAmbiental> findByPocoIdOrderByDataEventoDesc(Long pocoId);

}
