package com.sistema.fazenda.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.fazenda.datables.Datatables;
import com.sistema.fazenda.datables.DatatablesColunas;
import com.sistema.fazenda.model.Fluxo;
import com.sistema.fazenda.model.Propriedade;
import com.sistema.fazenda.model.Usuario;
import com.sistema.fazenda.repository.FluxoRepository;
import com.sistema.fazenda.repository.PropriedadeRepository;
import com.sistema.fazenda.repository.UsuarioRepository;
import com.zaxxer.hikari.HikariDataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class FluxoService {
	
	@Autowired
	private FluxoRepository repository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PropriedadeRepository propriedadeRepository;
	
	@Autowired
	private HikariDataSource hikariDataSource;
		
	@Autowired
	private Datatables dataTables;
	
	@Transactional(readOnly=false)
	public void salvar(Fluxo fluxo) {
		repository.save(fluxo);		
	}

	@Transactional(readOnly=true)
	public Map<String,Object> buscarFluxos(HttpServletRequest http) {
		
		dataTables.setRequest(http);
		dataTables.setColunas(DatatablesColunas.FLUXO);
		
		Page<?> page;
		
		if(dataTables.getSearch().isEmpty()) {
			page = repository.findAll(dataTables.getPageable());
		}else {
			page = repository.findAllByItemOrPropriedade(dataTables.getSearch(), dataTables.getPageable());
		}
		
		return dataTables.getResponse(page);
	}

	@Transactional(readOnly=true)
	public Fluxo buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly=false)
	public void remover(Long id) {
		repository.deleteById(id);		
	}
	
	public void relatorio (HttpServletResponse response, String email, Fluxo fluxo) throws JRException, SQLException, IOException {
		
		//Preenchendo os parâmetros do relatório
		Map<String, Object> parametros = new HashMap<>();
		Optional<Usuario> proprietario = usuarioRepository.findByEmail(email);
		Optional<Propriedade> propriedade = propriedadeRepository.findById(fluxo.getPropriedade().getId());	
		
		parametros.put("PROPRIEDADE_ID", fluxo.getPropriedade().getId());
		parametros.put("PROPRIETARIO", proprietario.get().getNome());
		parametros.put("PROPRIEDADE_NOME", propriedade.get().getNome());
		parametros.put("ANO", fluxo.getData().getYear());
		parametros.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
		
		// Pega o arquivo .jasper localizado em resources
		InputStream jasperStream = this.getClass().getResourceAsStream("/relatorios/resultado/Resultado.jasper");
		
		// Cria o objeto JasperReport com o Stream do arquivo jasper
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		// Passa para o JasperPrint o relatório e os parâmetros
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, hikariDataSource.getConnection());

		// Configura a resposta para o tipo PDF
		response.setContentType("application/pdf");
		// Define que o arquivo pode ser visualizado no navegador e também nome final do arquivo
		// para fazer download do relatório troque 'inline' por 'attachment'
		response.setHeader("Content-Disposition", "attachment; filename=resultado-consolidado.pdf");

		// Faz a exportação do relatório para o HttpServletResponse
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
	}

}
