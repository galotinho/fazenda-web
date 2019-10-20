package com.sistema.fazenda.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.fazenda.datables.Datatables;
import com.sistema.fazenda.datables.DatatablesColunas;
import com.sistema.fazenda.model.Propriedade;
import com.sistema.fazenda.model.Usuario;
import com.sistema.fazenda.repository.PropriedadeRepository;
import com.sistema.fazenda.repository.UsuarioRepository;

@Service
public class PropriedadeService {
	
	@Autowired
	private PropriedadeRepository repository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private Datatables dataTables;

	
	@Transactional(readOnly=false)
	public void salvar(Propriedade propriedade) {
		propriedade.setUsuario(usuarioRepository.findById(propriedade.getUsuario().getId()).get());
		repository.save(propriedade);		
	}

	@Transactional(readOnly=true)
	public Map<String,Object> buscarPropriedades(HttpServletRequest http) {
		
		dataTables.setRequest(http);
		dataTables.setColunas(DatatablesColunas.PROPRIEDADE);
		
		Page<?> page;
		
		if(dataTables.getSearch().isEmpty()) {
			page = repository.findAll(dataTables.getPageable());
		}else {
			page = repository.findAllByNome(dataTables.getSearch(), dataTables.getPageable());
		}
		
		return dataTables.getResponse(page);
	}

	@Transactional(readOnly=true)
	public Propriedade buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly=false)
	public void remover(Long id) {
		repository.deleteById(id);		
	}

		
	@Transactional(readOnly = true)
	public List<Usuario> buscarProprietarios() {
		return usuarioRepository.findAllByProprietario("PROPRIETARIO");
	}

	@Transactional(readOnly = true)
	public List<Propriedade> buscarPropriedadesPorProprietario(String email) {
		
		Optional<Usuario> proprietario = usuarioRepository.findByEmail(email);
		
		return repository.findAllPropriedadesPorProprietario(proprietario.get().getId());
		
	}
}
