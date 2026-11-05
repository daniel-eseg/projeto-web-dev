package br.edu.eseg.petrolifera.oilDynamics.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.edu.eseg.petrolifera.oilDynamics.service.RegistroNaoEncontradoException;

/** Tratamento de erros comum a todos os controllers. */
@ControllerAdvice
public class TratamentoErros {

	@ExceptionHandler(RegistroNaoEncontradoException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String naoEncontrado(RegistroNaoEncontradoException e, Model model) {
		model.addAttribute("mensagem", e.getMessage());
		return "erro";
	}

}
