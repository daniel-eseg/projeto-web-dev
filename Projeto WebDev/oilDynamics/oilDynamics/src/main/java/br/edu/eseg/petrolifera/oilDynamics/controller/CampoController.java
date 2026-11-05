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

import br.edu.eseg.petrolifera.oilDynamics.model.Campo;
import br.edu.eseg.petrolifera.oilDynamics.service.CampoService;
import br.edu.eseg.petrolifera.oilDynamics.service.RegraNegocioException;
import jakarta.validation.Valid;

/**
 * Fluxo Controller -> Model -> Thymeleaf: cada método coloca dados no Model
 * e devolve o nome do template (pasta templates/), ou um redirect.
 */
@Controller
@RequestMapping("/campos")
public class CampoController {

	private final CampoService campoService;

	public CampoController(CampoService campoService) {
		this.campoService = campoService;
	}

	@GetMapping
	public String listar(Model model) {
		model.addAttribute("campos", campoService.listar());
		return "campos/lista";
	}

	@GetMapping("/novo")
	public String novo(Model model) {
		model.addAttribute("campo", new Campo());
		return "campos/form";
	}

	@GetMapping("/{id}/editar")
	public String editar(@PathVariable Long id, Model model) {
		model.addAttribute("campo", campoService.buscar(id));
		return "campos/form";
	}

	/** @Valid aplica as anotações da entidade; os erros ficam no BindingResult e aparecem junto aos campos. */
	@PostMapping("/salvar")
	public String salvar(@Valid @ModelAttribute("campo") Campo campo, BindingResult result,
			RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return "campos/form";
		}
		try {
			campoService.salvar(campo);
		} catch (RegraNegocioException e) {
			ErrosFormulario.registrar(result, e);
			return "campos/form";
		}
		redirect.addFlashAttribute("sucesso", "Campo " + campo.getCodigo() + " salvo com sucesso.");
		return "redirect:/campos";
	}

	@PostMapping("/{id}/excluir")
	public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
		try {
			campoService.excluir(id);
			redirect.addFlashAttribute("sucesso", "Campo excluído.");
		} catch (RegraNegocioException e) {
			redirect.addFlashAttribute("erro", e.getMessage());
		}
		return "redirect:/campos";
	}

}
