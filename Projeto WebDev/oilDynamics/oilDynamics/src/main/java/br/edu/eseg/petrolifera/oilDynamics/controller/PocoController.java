package br.edu.eseg.petrolifera.oilDynamics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.eseg.petrolifera.oilDynamics.model.Poco;
import br.edu.eseg.petrolifera.oilDynamics.model.StatusPoco;
import br.edu.eseg.petrolifera.oilDynamics.service.CampoService;
import br.edu.eseg.petrolifera.oilDynamics.service.PocoService;
import br.edu.eseg.petrolifera.oilDynamics.service.RegraNegocioException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/pocos")
public class PocoController {

	private final PocoService pocoService;
	private final CampoService campoService;

	public PocoController(PocoService pocoService, CampoService campoService) {
		this.pocoService = pocoService;
		this.campoService = campoService;
	}

	/** GET /pocos ou /pocos?status=ATIVO (filtro por status, RF-12). */
	@GetMapping
	public String listar(@RequestParam(required = false) StatusPoco status, Model model) {
		model.addAttribute("pocos", pocoService.listar(status));
		model.addAttribute("statusFiltro", status);
		model.addAttribute("statusOpcoes", StatusPoco.values());
		return "pocos/lista";
	}

	@GetMapping("/novo")
	public String novo(Model model) {
		model.addAttribute("poco", new Poco());
		return prepararFormulario(model);
	}

	@GetMapping("/{id}/editar")
	public String editar(@PathVariable Long id, Model model) {
		model.addAttribute("poco", pocoService.buscar(id));
		return prepararFormulario(model);
	}

	@PostMapping("/salvar")
	public String salvar(@Valid @ModelAttribute("poco") Poco poco, BindingResult result, Model model,
			RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return prepararFormulario(model);
		}
		try {
			pocoService.salvar(poco);
		} catch (RegraNegocioException e) {
			ErrosFormulario.registrar(result, e);
			return prepararFormulario(model);
		}
		redirect.addFlashAttribute("sucesso", "Poço " + poco.getCodigo() + " salvo com sucesso.");
		return "redirect:/pocos";
	}

	@PostMapping("/{id}/excluir")
	public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
		pocoService.excluir(id);
		redirect.addFlashAttribute("sucesso", "Poço excluído.");
		return "redirect:/pocos";
	}

	/** Listas usadas nos <select> do formulário. */
	private String prepararFormulario(Model model) {
		model.addAttribute("campos", campoService.listar());
		model.addAttribute("statusOpcoes", StatusPoco.values());
		return "pocos/form";
	}

}
