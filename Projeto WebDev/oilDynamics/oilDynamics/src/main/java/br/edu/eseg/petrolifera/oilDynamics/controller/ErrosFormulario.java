package br.edu.eseg.petrolifera.oilDynamics.controller;

import org.springframework.validation.BindingResult;

import br.edu.eseg.petrolifera.oilDynamics.service.RegraNegocioException;

/** Converte uma RegraNegocioException em erro do formulário, para o Thymeleaf exibir junto ao campo. */
final class ErrosFormulario {

	private ErrosFormulario() {
	}

	static void registrar(BindingResult result, RegraNegocioException e) {
		if (e.getCampo() != null) {
			result.rejectValue(e.getCampo(), "regra", e.getMessage());
		} else {
			result.reject("regra", e.getMessage());
		}
	}

}
