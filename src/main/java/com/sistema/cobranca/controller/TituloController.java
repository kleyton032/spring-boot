package com.sistema.cobranca.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sistema.cobranca.model.StatusTitulo;
import com.sistema.cobranca.model.Titulo;
import com.sistema.cobranca.repository.TitulosRepository;

@Controller
@RequestMapping("/titulos")
public class TituloController {

	@Autowired
	private TitulosRepository titulosRepository;
	
	private static final String CADASTRO_VIEW = "CadastroTitulo";

	@RequestMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(new Titulo());
		return mv;
	}

	@RequestMapping
	public ModelAndView pesquisar() {
		List<Titulo> todosTitulos = titulosRepository.findAll();
		ModelAndView mv = new ModelAndView("PesquisaTitulo");
		mv.addObject("titulos", todosTitulos);
		return mv;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes attributes) {

		if (errors.hasErrors()) {
			return CADASTRO_VIEW;
		}

		try {
			titulosRepository.save(titulo);
	
			attributes.addFlashAttribute("mensagem", "Título salvo com sucesso!");
			return "redirect:/titulos/novo";
		}catch (DataIntegrityViolationException e) {
			errors.rejectValue("dataVencimento", null, "Formato de data Inválido");
			return CADASTRO_VIEW;
		}
	}
	
	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable Long codigo) {
		Titulo titulo = titulosRepository.findOne(codigo);
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(titulo);
		return mv;
	}
	
	@RequestMapping(value="{codigo}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		titulosRepository.delete(codigo);
		
		attributes.addFlashAttribute("mensagem", "Título excluído com sucesso!");
		return "redirect:/titulos";
	}

	@ModelAttribute("todosStatusTitulos")
	public List<StatusTitulo> todosStatusTitulo() {
		return Arrays.asList(StatusTitulo.values());
	}

}
