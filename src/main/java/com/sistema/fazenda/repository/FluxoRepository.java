package com.sistema.fazenda.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sistema.fazenda.model.Fluxo;

public interface FluxoRepository extends JpaRepository<Fluxo, Long>{

	@Query("select f from Fluxo f where f.item like %:search% "
			+ "or f.propriedade.nome like %:search% "
			+ "or f.natureza like %:search%")
	Page<Fluxo> findAllByItemOrPropriedade(String search, Pageable pageable);
}
