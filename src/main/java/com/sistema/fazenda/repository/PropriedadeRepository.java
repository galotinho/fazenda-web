package com.sistema.fazenda.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sistema.fazenda.model.Propriedade;

public interface PropriedadeRepository extends JpaRepository<Propriedade, Long>{

	@Query("select p from Propriedade p where p.nome like :nome%")
	Page<Propriedade> findAllByNome(String nome, Pageable pageable);

	@Query("select p from Propriedade p where p.usuario.id = :id")
	List<Propriedade> findAllPropriedadesPorProprietario(Long id);
}
