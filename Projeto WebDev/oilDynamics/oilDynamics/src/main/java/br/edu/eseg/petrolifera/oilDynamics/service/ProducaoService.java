package br.edu.eseg.petrolifera.oilDynamics.service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.eseg.petrolifera.oilDynamics.model.Poco;
import br.edu.eseg.petrolifera.oilDynamics.model.Producao;
import br.edu.eseg.petrolifera.oilDynamics.model.Refinaria;
import br.edu.eseg.petrolifera.oilDynamics.model.StatusPoco;
import br.edu.eseg.petrolifera.oilDynamics.repository.PocoRepository;
import br.edu.eseg.petrolifera.oilDynamics.repository.ProducaoRepository;
import br.edu.eseg.petrolifera.oilDynamics.repository.RefinariaRepository;

@Service
public class ProducaoService {

	private static final DateTimeFormatter DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private final ProducaoRepository producaoRepository;
	private final PocoRepository pocoRepository;
	private final RefinariaRepository refinariaRepository;

	public ProducaoService(ProducaoRepository producaoRepository, PocoRepository pocoRepository,
			RefinariaRepository refinariaRepository) {
		this.producaoRepository = producaoRepository;
		this.pocoRepository = pocoRepository;
		this.refinariaRepository = refinariaRepository;
	}

	public List<Producao> listar() {
		return producaoRepository.findAllByOrderByDataProducaoDescCodigoAsc();
	}

	public Producao buscar(Long id) {
		return producaoRepository.findById(id)
				.orElseThrow(() -> new RegistroNaoEncontradoException("Produção não encontrada (id " + id + ")."));
	}

	/**
	 * RF-04. Volumes não negativos e eficiência 0–100 (RN-05) já são validados pelas
	 * anotações da entidade; aqui ficam as regras que dependem do banco.
	 */
	@Transactional
	public Producao salvar(Producao dados) {
		String codigo = dados.getCodigo().trim();
		boolean novo = dados.getId() == null;
		Poco poco = carregarPoco(dados);
		Producao existente = novo ? null : buscar(dados.getId());

		if (novo ? producaoRepository.existsByCodigo(codigo)
				: producaoRepository.existsByCodigoAndIdNot(codigo, dados.getId())) {
			throw new RegraNegocioException("codigo", "Já existe uma produção com o código " + codigo + ".");
		}

		// RN-03: só se registra produção em poço Ativo (vale no cadastro e quando o poço é trocado na edição)
		boolean pocoMudou = existente != null && !existente.getPoco().getId().equals(poco.getId());
		if ((novo || pocoMudou) && poco.getStatusPoco() != StatusPoco.ATIVO) {
			throw new RegraNegocioException("poco", "Só é possível registrar produção em poço Ativo. O poço "
					+ poco.getCodigo() + " está " + poco.getStatusPoco().getDescricao() + ".");
		}

		// RN-03: a data não pode ser anterior ao início do poço
		if (dados.getDataProducao().isBefore(poco.getDataInicio())) {
			throw new RegraNegocioException("dataProducao", "A data não pode ser anterior ao início do poço "
					+ poco.getCodigo() + " (" + poco.getDataInicio().format(DATA_BR) + ").");
		}

		// RN-04: uma produção por poço e data
		boolean duplicada = novo
				? producaoRepository.existsByPocoIdAndDataProducao(poco.getId(), dados.getDataProducao())
				: producaoRepository.existsByPocoIdAndDataProducaoAndIdNot(poco.getId(), dados.getDataProducao(), dados.getId());
		if (duplicada) {
			throw new RegraNegocioException("dataProducao", "Já existe produção do poço " + poco.getCodigo()
					+ " em " + dados.getDataProducao().format(DATA_BR) + ".");
		}

		Refinaria refinaria = carregarRefinaria(dados);

		if (novo) {
			dados.setCodigo(codigo);
			dados.setPoco(poco);
			dados.setRefinariaDestino(refinaria);
			return producaoRepository.save(dados);
		}

		existente.setCodigo(codigo);
		existente.setVolumeOleoBarris(dados.getVolumeOleoBarris());
		existente.setVolumeGasMetrosCubicos(dados.getVolumeGasMetrosCubicos());
		existente.setEficienciaPercentual(dados.getEficienciaPercentual());
		existente.setCustoOperacional(dados.getCustoOperacional());
		existente.setDataProducao(dados.getDataProducao());
		existente.setPoco(poco);
		existente.setRefinariaDestino(refinaria);
		return existente;
	}

	@Transactional
	public void excluir(Long id) {
		producaoRepository.delete(buscar(id));
	}

	private Poco carregarPoco(Producao dados) {
		if (dados.getPoco() == null || dados.getPoco().getId() == null) {
			throw new RegraNegocioException("poco", "Selecione o poço.");
		}
		return pocoRepository.findById(dados.getPoco().getId())
				.orElseThrow(() -> new RegraNegocioException("poco", "O poço selecionado não existe."));
	}

	/** RF-05: refinaria de destino é opcional. */
	private Refinaria carregarRefinaria(Producao dados) {
		if (dados.getRefinariaDestino() == null || dados.getRefinariaDestino().getId() == null) {
			return null;
		}
		return refinariaRepository.findById(dados.getRefinariaDestino().getId())
				.orElseThrow(() -> new RegraNegocioException("refinariaDestino", "A refinaria selecionada não existe."));
	}

}
