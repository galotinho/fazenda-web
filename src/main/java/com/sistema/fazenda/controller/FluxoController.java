package com.sistema.fazenda.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.sistema.fazenda.model.Fluxo;
import com.sistema.fazenda.service.FluxoService;

import net.sf.jasperreports.engine.JRException;

@Controller
@RequestMapping("fluxo")
public class FluxoController {

	@Autowired
	private FluxoService service;
	
	@GetMapping({"","/"})
	public String abrir(Fluxo fluxo) {
		return "proprietario/fluxo";
	}
	
	@PostMapping("/salvar")
	public String salvar(Fluxo fluxo, RedirectAttributes attr) {
		service.salvar(fluxo);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");		
		return "redirect:/fluxo";
	}
	
	@GetMapping("/datatables/server")
	public ResponseEntity<?> getFluxos(HttpServletRequest request){
		return ResponseEntity.ok(service.buscarFluxos(request));
	}
	
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model){
		model.addAttribute("fluxo", service.buscarPorId(id));
		return  "proprietario/fluxo";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr){
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
		return "redirect:/fluxo";
	}
	
	//Ir para a página de escolha da propriedade para gerar resultado consolidado
	@GetMapping("/resultado")
	public String resultado(Fluxo fluxo) {
		return "proprietario/resultado";
	}
	
	//Gerar o relatório consolidado
	@PostMapping("/relatorio")
	public String relatorio(Fluxo fluxo, HttpServletResponse response, @AuthenticationPrincipal User usuario) throws JRException, SQLException, IOException {
		
		service.relatorio(response, usuario.getUsername(), fluxo);
	
		return "proprietario/resultado";
	}
}
