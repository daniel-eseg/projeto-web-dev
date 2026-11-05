package br.edu.eseg.petrolifera.oilDynamics.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.eseg.petrolifera.oilDynamics.model.Campo;
import br.edu.eseg.petrolifera.oilDynamics.model.Poco;
import br.edu.eseg.petrolifera.oilDynamics.model.StatusPoco;
import br.edu.eseg.petrolifera.oilDynamics.repository.CampoRepository;
import br.edu.eseg.petrolifera.oilDynamics.repository.PocoRepository;

@Service
public class PocoService {

	private final PocoRepository pocoRepository;
	private final CampoRepository campoRepository;

	public PocoService(PocoRepository pocoRepository, CampoRepository campoRepository) {
		this.pocoRepository = pocoRepository;
		this.campoRepository = campoRepository;
	}

	/** Lista todos ou só os do status informado (RF-12). */
	public List<Poco> listar(StatusPoco status) {
		if (status == null) {
			return pocoRepository.findAllByOrderByCodigoAsc();
		}
		return pocoRepository.findByStatusPocoOrderByCodigoAsc(status);
	}

	public Poco buscar(Long id) {
		return pocoRepository.findById(id)
				.orElseThrow(() -> new RegistroNaoEncontradoException("Poço não encontrado (id " + id + ")."));
	}

	@Transactional
	public Poco salvar(Poco dados) {
		String codigo = dados.getCodigo().trim();
		Campo campo = carregarCampo(dados);

		if (dados.getId() == null) {
			if (pocoRepository.existsByCodigo(codigo)) {
				throw new RegraNegocioException("codigo", "Já existe um poço com o código " + codigo + ".");
			}
			dados.setCodigo(codigo);
			dados.setCampo(campo);
			return pocoRepository.save(dados);
		}

		if (pocoRepository.existsByCodigoAndIdNot(codigo, dados.getId())) {
			throw new RegraNegocioException("codigo", "Já existe outro poço com o código " + codigo + ".");
		}
		Poco existente = buscar(dados.getId());

		// RF-03 / RN-02: bloqueia transição de status inválida
		StatusPoco atual = existente.getStatusPoco();
		StatusPoco novo = dados.getStatusPoco();
		if (!atual.podeMudarPara(novo)) {
			throw new RegraNegocioException("statusPoco",
					"Transição inválida: um poço " + atual.getDescricao() + " não pode passar para " + novo.getDescricao() + ".");
		}

		existente.setCodigo(codigo);
		existente.setTipoPoco(dados.getTipoPoco());
		existente.setVazaoBarrisPorDia(dados.getVazaoBarrisPorDia());
		existente.setStatusPoco(novo);
		existente.setDataInicio(dados.getDataInicio());
		existente.setCampo(campo);
		return existente;
	}

	/** Exclui o poço e, por cascade, as produções dele. */
	@Transactional
	public void excluir(Long id) {
		pocoRepository.delete(buscar(id));
	}

	/** RF-02: todo poço pertence obrigatoriamente a um campo. */
	private Campo carregarCampo(Poco dados) {
		if (dados.getCampo() == null || dados.getCampo().getId() == null) {
			throw new RegraNegocioException("campo", "Selecione o campo do poço.");
		}
		return campoRepository.findById(dados.getCampo().getId())
				.orElseThrow(() -> new RegraNegocioException("campo", "O campo selecionado não existe."));
	}

}
