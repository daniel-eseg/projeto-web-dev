package br.edu.eseg.petrolifera.oilDynamics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	/** Por enquanto a página inicial é a lista de campos (o painel do RF-12 entra depois). */
	@GetMapping("/")
	public String inicio() {
		return "redirect:/campos";
	}

}
