package com.sistema.fazenda.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sistema.fazenda.model.Propriedade;
import com.sistema.fazenda.service.PropriedadeService;


@Controller
@RequestMapping("propriedade")
public class PropriedadeController {
	
	@Autowired
	private PropriedadeService service;
	
	@GetMapping({"","/"})
	public String abrir(Propriedade propriedade) {
		return "administrador/cadastro-fazenda";
	}
	
	@PostMapping("/salvar")
	public String salvar(Propriedade propriedade, RedirectAttributes attr) {
		service.salvar(propriedade);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");		
		return "redirect:/propriedade";
	}
	
	@GetMapping("/datatables/server")
	public ResponseEntity<?> getPropriedades(HttpServletRequest request){
		return ResponseEntity.ok(service.buscarPropriedades(request));
	}
	
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model){
		model.addAttribute("propriedade", service.buscarPorId(id));
		return "administrador/cadastro-fazenda";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr){
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
		return "redirect:/propriedade";
	}
	
	
	@GetMapping("/proprietario/listar")
	public ResponseEntity<?> getProprietarios() {
		
		return ResponseEntity.ok(service.buscarProprietarios());
	}
	
	@GetMapping("/listar")
	public ResponseEntity<?> getPropriedadesPorProprietario(@AuthenticationPrincipal User usuario) {
		return ResponseEntity.ok(service.buscarPropriedadesPorProprietario(usuario.getUsername()));
	}

}
