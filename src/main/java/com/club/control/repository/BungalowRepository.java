package com.club.control.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.club.control.entity.BungalowEntity;

public interface BungalowRepository extends JpaRepository<BungalowEntity, Long>{

	Optional<BungalowEntity> findByCodigo (String codigo);
	
	//Filtro para buscar los bungalows disponibles
	Page<BungalowEntity> findByDisponible(boolean disponible, Pageable pageable);
}
