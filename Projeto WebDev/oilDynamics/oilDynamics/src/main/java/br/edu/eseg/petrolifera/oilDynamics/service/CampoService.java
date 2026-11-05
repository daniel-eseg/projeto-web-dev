package br.edu.eseg.petrolifera.oilDynamics.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.eseg.petrolifera.oilDynamics.model.Campo;
import br.edu.eseg.petrolifera.oilDynamics.model.StatusPoco;
import br.edu.eseg.petrolifera.oilDynamics.repository.CampoRepository;
import br.edu.eseg.petrolifera.oilDynamics.repository.PocoRepository;

@Service
public class CampoService {

	private final CampoRepository campoRepository;
	private final PocoRepository pocoRepository;

	// injeção de dependência pelo construtor
	public CampoService(CampoRepository campoRepository, PocoRepository pocoRepository) {
		this.campoRepository = campoRepository;
		this.pocoRepository = pocoRepository;
	}

	public List<Campo> listar() {
		return campoRepository.findAllByOrderByCodigoAsc();
	}

	public Campo buscar(Long id) {
		return campoRepository.findById(id)
				.orElseThrow(() -> new RegistroNaoEncontradoException("Campo não encontrado (id " + id + ")."));
	}

	/** Cadastra (id nulo) ou atualiza (id preenchido) um campo. */
	@Transactional
	public Campo salvar(Campo dados) {
		String codigo = dados.getCodigo().trim();

		if (dados.getId() == null) {
			// RF-01: código único
			if (campoRepository.existsByCodigo(codigo)) {
				throw new RegraNegocioException("codigo", "Já existe um campo com o código " + codigo + ".");
			}
			dados.setCodigo(codigo);
			return campoRepository.save(dados);
		}

		if (campoRepository.existsByCodigoAndIdNot(codigo, dados.getId())) {
			throw new RegraNegocioException("codigo", "Já existe outro campo com o código " + codigo + ".");
		}
		// Na edição carregamos o registro do banco e copiamos só os campos do formulário.
		// Salvar o objeto do formulário direto apagaria os poços (lista vazia + orphanRemoval).
		Campo existente = buscar(dados.getId());
		existente.setCodigo(codigo);
		existente.setNomeBacia(dados.getNomeBacia());
		existente.setEstado(dados.getEstado());
		existente.setAmbiente(dados.getAmbiente());
		existente.setStatus(dados.getStatus());
		return existente; // @Transactional: o Hibernate grava as alterações no fim do método
	}

	/** RN-06: excluir o campo exclui poços e produções (cascade), mas só se não houver poço ativo. */
	@Transactional
	public void excluir(Long id) {
		Campo campo = buscar(id);
		if (pocoRepository.existsByCampoIdAndStatusPoco(id, StatusPoco.ATIVO)) {
			throw new RegraNegocioException(
					"O campo " + campo.getCodigo() + " não pode ser excluído porque tem poço ativo.");
		}
		campoRepository.delete(campo);
	}

}
