package br.edu.eseg.petrolifera.oilDynamics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.eseg.petrolifera.oilDynamics.model.Producao;
import br.edu.eseg.petrolifera.oilDynamics.repository.RefinariaRepository;
import br.edu.eseg.petrolifera.oilDynamics.service.PocoService;
import br.edu.eseg.petrolifera.oilDynamics.service.ProducaoService;
import br.edu.eseg.petrolifera.oilDynamics.service.RegraNegocioException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/producoes")
public class ProducaoController {

	private final ProducaoService producaoService;
	private final PocoService pocoService;
	private final RefinariaRepository refinariaRepository;

	public ProducaoController(ProducaoService producaoService, PocoService pocoService,
			RefinariaRepository refinariaRepository) {
		this.producaoService = producaoService;
		this.pocoService = pocoService;
		this.refinariaRepository = refinariaRepository;
	}

	@GetMapping
	public String listar(Model model) {
		model.addAttribute("producoes", producaoService.listar());
		return "producoes/lista";
	}

	@GetMapping("/novo")
	public String novo(Model model) {
		model.addAttribute("producao", new Producao());
		return prepararFormulario(model);
	}

	@GetMapping("/{id}/editar")
	public String editar(@PathVariable Long id, Model model) {
		model.addAttribute("producao", producaoService.buscar(id));
		return prepararFormulario(model);
	}

	@PostMapping("/salvar")
	public String salvar(@Valid @ModelAttribute("producao") Producao producao, BindingResult result, Model model,
			RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return prepararFormulario(model);
		}
		try {
			producaoService.salvar(producao);
		} catch (RegraNegocioException e) {
			ErrosFormulario.registrar(result, e);
			return prepararFormulario(model);
		}
		redirect.addFlashAttribute("sucesso", "Produção " + producao.getCodigo() + " salva com sucesso.");
		return "redirect:/producoes";
	}

	@PostMapping("/{id}/excluir")
	public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
		producaoService.excluir(id);
		redirect.addFlashAttribute("sucesso", "Produção excluída.");
		return "redirect:/producoes";
	}

	private String prepararFormulario(Model model) {
		model.addAttribute("pocos", pocoService.listar(null));
		model.addAttribute("refinarias", refinariaRepository.findAllByOrderByCodigoAsc());
		return "producoes/form";
	}

}
