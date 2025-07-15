package com.club.control.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.club.control.entity.ClienteBungalowEntity;

public interface ClienteBungalowRepository extends JpaRepository<ClienteBungalowEntity, Long>{

	//Filtro para buscar servicios de bungalows por dia
	Page<ClienteBungalowEntity> findByFechaInicio (LocalDate fechaInicio, Pageable pageable);
}
