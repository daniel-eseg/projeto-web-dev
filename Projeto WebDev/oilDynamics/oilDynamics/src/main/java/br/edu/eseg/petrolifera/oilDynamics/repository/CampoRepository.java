package br.edu.eseg.petrolifera.oilDynamics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.eseg.petrolifera.oilDynamics.model.Campo;

/** O Spring Data gera a implementação a partir do nome dos métodos. */
public interface CampoRepository extends JpaRepository<Campo, Long> {

	List<Campo> findAllByOrderByCodigoAsc();

	/** Usado no cadastro: o código não pode existir. */
	boolean existsByCodigo(String codigo);

	/** Usado na edição: o código não pode existir em OUTRO campo. */
	boolean existsByCodigoAndIdNot(String codigo, Long id);

}
