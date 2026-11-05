package br.edu.eseg.petrolifera.oilDynamics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.eseg.petrolifera.oilDynamics.model.Refinaria;

public interface RefinariaRepository extends JpaRepository<Refinaria, Long> {

	List<Refinaria> findAllByOrderByCodigoAsc();

}
